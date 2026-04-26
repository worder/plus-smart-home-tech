package ru.yandex.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;

import java.util.UUID;

public interface DatabaseSoppingCartRepository extends ShoppingCartRepository, JpaRepository<ShoppingCart, UUID> {
}
