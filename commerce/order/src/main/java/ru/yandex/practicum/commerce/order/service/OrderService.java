package ru.yandex.practicum.commerce.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.request.ReturnOrderRequest;

import java.util.UUID;

public interface OrderService {
    Page<OrderDto> findOrders(String username, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ReturnOrderRequest request);

    OrderDto paymentSuccess(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto deliverySuccess(UUID orderId);

    OrderDto deliveryFailed(UUID orderId);

    OrderDto completed(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto assemblySuccess(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);
}
