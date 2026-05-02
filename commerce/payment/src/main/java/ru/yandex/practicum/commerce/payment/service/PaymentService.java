package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto request);

    double calculateProductCost(OrderDto request);

    double calculateTotalCost(OrderDto request);

    void paymentSuccess(UUID paymentId);

    void paymentFailed(UUID paymentId);
}
