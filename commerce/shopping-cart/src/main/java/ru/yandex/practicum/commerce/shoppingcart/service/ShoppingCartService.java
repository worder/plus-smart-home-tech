package ru.yandex.practicum.commerce.shoppingcart.service;

import ru.yandex.practicum.commerce.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartService {
    Optional<ShoppingCartDto> getShoppingCart(String username);

    void deactivateCart(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Integer> products);

    ShoppingCartDto removeProducts(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
