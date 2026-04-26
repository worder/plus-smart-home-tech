package ru.yandex.practicum.commerce.warehouse.repository;

import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseProductRepository {
    boolean existsByProductId(UUID productId);

    Optional<WarehouseProduct> findByProductId(UUID productId);

    Collection<WarehouseProduct> findAllByProductIdIn(Collection<UUID> productIds);

    WarehouseProduct save(WarehouseProduct warehouseProduct);
}
