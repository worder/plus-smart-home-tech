package ru.yandex.practicum.commerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.payment.model.Payment;

import java.util.UUID;

public interface DatabasePaymentRepository extends PaymentRepository, JpaRepository<Payment, UUID> {
}
