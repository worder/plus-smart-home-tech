package ru.yandex.practicum.commerce.shoppingcart.repository;

import ru.yandex.practicum.commerce.dto.ShoppingCartState;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository {
    Optional<ShoppingCart> findByUsernameAndState(String username, ShoppingCartState state);

    ShoppingCart save(ShoppingCart shoppingCart);
}
