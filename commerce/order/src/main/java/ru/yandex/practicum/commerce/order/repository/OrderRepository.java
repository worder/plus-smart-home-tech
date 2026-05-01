package ru.yandex.practicum.commerce.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.order.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Page<Order> findAllByShoppingCartId(UUID shoppingCartId, Pageable pageable);
}
