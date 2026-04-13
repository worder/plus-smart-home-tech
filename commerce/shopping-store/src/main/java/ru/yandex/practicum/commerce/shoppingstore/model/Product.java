package ru.yandex.practicum.commerce.shoppingstore.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductState;
import ru.yandex.practicum.commerce.dto.QuantityState;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;

    String productName;

    String description;

    String imageSrc;

    @Enumerated(EnumType.STRING)
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    ProductState productState;

    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;

    double price;
}
