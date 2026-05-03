package ru.yandex.practicum.commerce.order.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.ReturnOrderRequest;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.feign.PaymentClient;
import ru.yandex.practicum.commerce.dto.enums.OrderState;
import ru.yandex.practicum.commerce.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.feign.DeliveryClient;
import ru.yandex.practicum.commerce.feign.ShoppingCartClient;
import ru.yandex.practicum.commerce.feign.WarehouseClient;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final ShoppingCartClient shoppingCartClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    @Override
    public Page<OrderDto> findOrders(String username, Pageable pageable) {
        log.debug("Calling shoppingCartClient.getShoppingCart, username: {}",  username);
        UUID cartId = shoppingCartClient.getShoppingCart(username).getShoppingCartId();

        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Direction.DESC, "created_at");

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        return orderRepository
                .findAllByShoppingCartId(cartId, sortedPageable)
                .map(OrderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        Order order = Order.builder()
                .state(OrderState.NEW)
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(new HashMap<>(request.getShoppingCart().getProducts()))
                .build();

        // create new order
        order = orderRepository.save(order);
        log.info("Created new order: {} from request: {}", order, request);

        // make booking on warehouse
        BookedProductsDto bookedProducts;
        try {
            AssemblyProductsForOrderRequest assemblyRequest = AssemblyProductsForOrderRequest.builder()
                    .orderId(order.getOrderId())
                    .products(order.getProducts())
                    .build();

            log.debug("Calling warehouseClient.arrangeAssemblyForOrder, request: {}", assemblyRequest);
            bookedProducts = warehouseClient.arrangeAssemblyForOrder(assemblyRequest);
            log.info("warehouseClient.arrangeAssemblyForOrder success for order: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("warehouseClient.arrangeAssemblyForOrder failed for order: {}", order.getOrderId(), e);
            throw e;
        }

        // set delivery properties
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setFragile(bookedProducts.getFragile());

        // create delivery
        try {
            AddressDto warehouseAddress;
            try {
                warehouseAddress = warehouseClient.getWarehouseAddress();
                log.info("warehouseClient.getWarehouseAddress success, address: {}", warehouseAddress);
            } catch (Exception e){
                log.info("warehouseClient.getWarehouseAddress failed");
                throw e;
            }

            DeliveryDto deliveryRequest = DeliveryDto.builder()
                    .orderId(order.getOrderId())
                    .fromAddress(request.getDeliveryAddress())
                    .toAddress(warehouseAddress)
                    .build();

            log.debug("Calling deliveryClient.planDelivery, request: {}", deliveryRequest);
            DeliveryDto delivery = deliveryClient.planDelivery(deliveryRequest);
            order.setDeliveryId(delivery.getDeliveryId());
            log.info("deliveryClient.planDelivery success for order: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("deliveryClient.planDelivery failed for order: {}", order.getOrderId(), e);
            throw e;
        }

        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnOrder(ReturnOrderRequest request) {
        Order order = this.getOrder(request.getOrderId());

        try {
            log.debug("Calling warehouseClient.returnProducts, products: {}", order.getProducts());
            warehouseClient.returnProducts(request.getProducts());
            log.info("warehouseClient.returnProducts success for order: {}", order.getOrderId());
        }  catch (Exception e) {
            log.error("warehouseClient.returnProducts failed for order: {}", order.getOrderId(), e);
            throw e;
        }

        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("Return order: {} from request: {}", order, request);

        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        log.info("Payment successful for order: {}", orderId);
        return updateState(orderId, OrderState.PAID);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        log.info("Payment failed for order: {}", orderId);
        return updateState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto deliverySuccess(UUID orderId) {
        log.info("Delivery successful for order: {}", orderId);
        return updateState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        log.info("Delivery failed for order: {}", orderId);
        return updateState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto completed(UUID orderId) {
        log.info("Order completed: {}", orderId);
        return updateState(orderId, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = this.getOrder(orderId);
        order.setDeliveryPrice(deliveryClient.deliveryCost(OrderMapper.toDto(order)));
        log.info("Calculated delivery price for order: {} is {}", orderId, order.getDeliveryPrice());

        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = this.getOrder(orderId);
        order.setProductPrice(paymentClient.productCost(OrderMapper.toDto(order)));
        order.setTotalPrice(paymentClient.getTotalCost(OrderMapper.toDto(order)));
        log.info("Calculated total price for order: {} is {}", order.getOrderId(), order.getTotalPrice());

        // initiate payment process
        PaymentDto payment;
        try {
            OrderDto orderForPaymentRequest = OrderMapper.toDto(order);
            log.debug("Calling paymentClient.payment, order: {}", orderForPaymentRequest);
            payment = paymentClient.payment(orderForPaymentRequest);
            log.info("paymentClient.payment success for order: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("paymentClient.payment failed for order: {}", order.getOrderId(), e);
            throw e;
        }

        order.setPaymentId(payment.getPaymentId());
        log.info("Created payment: {} for order: {}", payment.getPaymentId(), order.getOrderId());

        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblySuccess(UUID orderId) {
        log.info("Assembly success for order: {}", orderId);
        return updateState(orderId, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        log.info("Assembly failed for order: {}", orderId);
        return updateState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private OrderDto updateState(UUID orderId, OrderState state) {
        Order order = this.getOrder(orderId);
        order.setState(state);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Product with id: [" + orderId + "] not found"));
    }
}
