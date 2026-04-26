package ru.yandex.practicum.commerce.shoppingstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingstore.model.Product;

import java.util.UUID;

public interface DatabaseShoppingStoreRepository extends ShoppingStoreRepository, JpaRepository<Product, UUID> {
}
