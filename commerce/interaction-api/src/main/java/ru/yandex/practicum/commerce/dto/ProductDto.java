package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.enums.ProductCategory;
import ru.yandex.practicum.commerce.dto.enums.ProductState;
import ru.yandex.practicum.commerce.dto.enums.QuantityState;

import java.util.UUID;

@Value
@Builder
public class ProductDto {
    UUID productId;

    @NotBlank
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters.")
    String productName;

    @NotBlank
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters.")
    String description;

    String imageSrc;

    @NotNull
    QuantityState quantityState;

    @NotNull
    ProductState productState;

    @NotNull
    ProductCategory productCategory;

    @NotNull
    double price;
}
