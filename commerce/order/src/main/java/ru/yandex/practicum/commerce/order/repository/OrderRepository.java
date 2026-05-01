package ru.yandex.practicum.commerce.order.repository;

import org.springframework.data.domain.Sort;
import ru.yandex.practicum.commerce.order.model.Order;

import java.util.Collection;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Collection<Order> findAllByShoppingCartId(UUID shoppingCartId, Sort sort);
}
