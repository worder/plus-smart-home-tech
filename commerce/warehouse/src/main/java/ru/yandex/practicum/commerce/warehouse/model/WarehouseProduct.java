package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "warehouse_product")
public class WarehouseProduct {
    @Id
    private UUID productId;

    private Integer quantity;

    private Boolean fragile = false;

    private Double depth;

    private Double height;

    private Double width;

    private Double weight;
}
