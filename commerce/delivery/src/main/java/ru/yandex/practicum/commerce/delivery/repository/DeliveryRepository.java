package ru.yandex.practicum.commerce.delivery.repository;

import ru.yandex.practicum.commerce.delivery.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID deliveryId);
}
