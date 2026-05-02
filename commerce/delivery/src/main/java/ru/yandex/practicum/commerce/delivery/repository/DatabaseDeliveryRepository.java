package ru.yandex.practicum.commerce.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.delivery.model.Delivery;

import java.util.UUID;

public interface DatabaseDeliveryRepository extends DeliveryRepository, JpaRepository<Delivery, UUID> {
}
