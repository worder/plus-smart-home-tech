package ru.yandex.practicum.commerce.shoppingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingStoreRepository {
    Product save(Product product);

    boolean existsByProductIdAndProductState(UUID productId, ProductState state);

    Optional<Product> findByProductId(UUID productId);

    Page<Product> findByProductCategory(ProductCategory category, Pageable pageable);
}
