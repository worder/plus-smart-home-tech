package ru.yandex.practicum.commerce.payment.repository;

import ru.yandex.practicum.commerce.payment.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByPaymentId(UUID paymentId);
}
