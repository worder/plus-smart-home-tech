package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void deliverySuccess(UUID deliveryId);

    void deliveryFail(UUID deliveryId);

    void deliveryPicked(UUID deliveryId);

    double calculateDeliveryCost(OrderDto order);
}
