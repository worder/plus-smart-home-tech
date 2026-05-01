package ru.yandex.practicum.commerce.order.service;

import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.request.ReturnOrderRequest;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {
    Collection<OrderDto> findOrders(String username);

    OrderDto createOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ReturnOrderRequest request);

    OrderDto payOrder(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto delivery(UUID orderId);

    OrderDto deliveryFailed(UUID orderId);

    OrderDto completed(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto assembly(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);
}
