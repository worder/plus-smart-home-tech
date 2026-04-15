package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "warehous_product")
public class WarehouseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    private Integer quantity;

    private Boolean fragile = false;

    private Double weight;

    private Double width;

    private Double length;

    private Double depth;
}
