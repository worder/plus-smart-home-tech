package ru.yandex.practicum.commerce.warehouse.repository;

import ru.yandex.practicum.commerce.warehouse.model.OrderBooking;

import java.util.Optional;
import java.util.UUID;

public interface OrderBookingsRepository {
    OrderBooking save(OrderBooking orderBooking);

    Optional<OrderBooking> findById(UUID bookingId);

    Optional<OrderBooking> findByOrderId(UUID orderId);
}
