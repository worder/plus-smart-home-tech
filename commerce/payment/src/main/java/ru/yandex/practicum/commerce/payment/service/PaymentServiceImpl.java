package ru.yandex.practicum.commerce.payment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.enums.PaymentStatus;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.feign.OrderClient;
import ru.yandex.practicum.commerce.feign.ShoppingStoreClient;
import ru.yandex.practicum.commerce.payment.error.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    private final static double TAX_FEE_MULTIPLIER = 0.1;

    @Override
    public PaymentDto createPayment(OrderDto request) {
        if (request.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Missing total price calculation");
        }
        if (request.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Missing delivery price calculation");
        }
        if (request.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Missing product price calculation");
        }

        double total = request.getTotalPrice();
        double delivery = request.getDeliveryPrice();
        double product = request.getProductPrice();
        double fee = total - (product + delivery);

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .totalPayment(total)
                .deliveryTotal(delivery)
                .productTotal(product)
                .feeTotal(fee)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment created: {} for order: {}", payment, request.getOrderId());

        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public double calculateProductCost(OrderDto request) {
        double productCost = 0.0;
        for (Map.Entry<UUID, Integer> entry : request.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            ProductDto product = shoppingStoreClient.getProduct(productId);
            productCost += product.getPrice() * quantity;
        }
        log.info("Product cost calculated: {} for order: {}", productCost, request.getOrderId());

        return productCost;
    }

    @Override
    public double calculateTotalCost(OrderDto request) {
        if (request.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Missing delivery price calculation");
        }

        final double delivery = request.getDeliveryPrice();
        final double product = request.getProductPrice();
        double total = delivery + product + calculateTax(product);
        log.info("Total cost calculated: {} for order: {}", total, request.getOrderId());

        return total;
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        try {
            orderClient.paymentSuccess(payment.getOrderId());
            log.info("orderClient.paymentSuccess success for orderId: {}", payment.getOrderId());
        } catch (Exception e) {
            log.error("orderClient.paymentSuccess failed for orderId: {}", payment.getOrderId(), e);
            throw e;
        }

        log.info("Payment success for id: {}", payment.getPaymentId());
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentStatus(PaymentStatus.FAILED);
        orderClient.paymentFailed(payment.getOrderId());
        log.info("Payment failed for id: {}", payment.getPaymentId());
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ItemNotFoundException("Payment with id: [" + paymentId + "] not found"));
    }

    private double calculateTax(double productPrice) {
        return productPrice * TAX_FEE_MULTIPLIER;
    }
}
