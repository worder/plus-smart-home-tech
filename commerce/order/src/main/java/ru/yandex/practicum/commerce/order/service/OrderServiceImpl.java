package ru.yandex.practicum.commerce.order.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.feign.ShoppingCartClient;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final ShoppingCartClient shoppingCartClient;

    @Override
    public Collection<OrderDto> findOrders(String username) {
        UUID cartId = shoppingCartClient.getShoppingCart(username).getShoppingCartId();

        Collection<Order> orders = orderRepository
                .findAllByShoppingCartId(cartId, Sort.by(Sort.Direction.DESC, "created_at"));
    }
}
