package ru.yandex.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;

import java.util.UUID;

@Repository
public interface DatabaseSoppingCartRepository extends ShoppingCartRepository, JpaRepository<ShoppingCart, UUID> {
}
