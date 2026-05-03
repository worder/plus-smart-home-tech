package ru.yandex.practicum.commerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.order.model.Order;

import java.util.UUID;

@Repository
public interface DatabaseOrderRepository extends OrderRepository, JpaRepository<Order, UUID> {
}
