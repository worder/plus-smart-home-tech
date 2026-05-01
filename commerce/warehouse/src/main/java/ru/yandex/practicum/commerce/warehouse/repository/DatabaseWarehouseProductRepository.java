package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

public interface DatabaseWarehouseProductRepository
        extends WarehouseProductRepository, JpaRepository<WarehouseProduct, Integer> {
}
