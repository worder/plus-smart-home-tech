package ru.yandex.practicum.commerce.order.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.ReturnOrderRequest;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.feign.PaymentClient;
import ru.yandex.practicum.commerce.dto.OrderDto;
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
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .state(OrderState.NEW)
                .products(new HashMap<>(request.getShoppingCart().getProducts()))
                .build();

        // create new order
        order = orderRepository.save(order);
        log.info("Created new order: {} from request: {}", order, request);

        // initiate payment process
        PaymentDto payment = paymentClient.payment(OrderMapper.toDto(order));
        order.setPaymentId(payment.getPaymentId());
        log.info("Created payment: {} for order: {}", payment.getPaymentId(), order.getOrderId());

        // create delivery
        DeliveryDto delivery = deliveryClient.planDelivery(DeliveryDto.builder()
                .orderId(order.getOrderId())
                .fromAddress(request.getDeliveryAddress())
                .toAddress(warehouseClient.getWarehouseAddress())
                .build());
        order.setDeliveryId(delivery.getDeliveryId());
        log.info("Created delivery: {} for order: {}", delivery.getDeliveryId(), order.getOrderId());

        // make booking on warehouse
        BookedProductsDto bookedProducts = warehouseClient
                .arrangeAssemblyForOrder(AssemblyProductsForOrderRequest.builder()
                        .orderId(order.getOrderId())
                        .products(order.getProducts())
                        .build());
        log.info("Arranged assembly for order: {}", order.getOrderId());

        // set delivery properties
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setFragile(bookedProducts.getFragile());

        // calculate product cost
        order.setProductPrice(paymentClient.productCost(OrderMapper.toDto(order)));

        return OrderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnOrder(ReturnOrderRequest request) {
        Order order = this.getOrder(request.getOrderId());
        warehouseClient.returnProducts(request.getProducts());

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
        order.setTotalPrice(paymentClient.getTotalCost(OrderMapper.toDto(order)));
        log.info("Calculated total price for order: {} is {}", order.getOrderId(), order.getTotalPrice());

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
