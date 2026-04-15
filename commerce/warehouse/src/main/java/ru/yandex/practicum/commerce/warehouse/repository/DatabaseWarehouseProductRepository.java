package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

@Repository
public interface DatabaseWarehouseProductRepository
        extends WarehouseProductRepository, JpaRepository<WarehouseProduct, Integer> {
}
