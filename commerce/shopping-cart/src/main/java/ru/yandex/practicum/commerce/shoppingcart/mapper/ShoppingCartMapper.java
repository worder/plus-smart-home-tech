package ru.yandex.practicum.commerce.shoppingcart.mapper;

import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;

import java.util.HashMap;

public class ShoppingCartMapper {
    public static ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(new HashMap<>(shoppingCart.getProducts()))
                .build();
    }

    public static ShoppingCart toEntity(ShoppingCartDto shoppingCartDto) {
        return ShoppingCart.builder()
                .shoppingCartId(shoppingCartDto.getShoppingCartId())
                .products(new HashMap<>(shoppingCartDto.getProducts()))
                .build();
    }
}
