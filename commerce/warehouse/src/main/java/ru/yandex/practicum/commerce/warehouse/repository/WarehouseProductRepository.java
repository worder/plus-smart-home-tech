package ru.yandex.practicum.commerce.warehouse.repository;

import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseProductRepository {
    boolean existsByProductId(UUID productId);

    Optional<WarehouseProduct> findByProductId(UUID productId);

    List<WarehouseProduct> findAllByProductId(Collection<UUID> productIds);

    WarehouseProduct save(WarehouseProduct warehouseProduct);
}
