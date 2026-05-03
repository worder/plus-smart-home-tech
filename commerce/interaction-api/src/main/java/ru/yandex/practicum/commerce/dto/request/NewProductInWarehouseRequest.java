package ru.yandex.practicum.commerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.ProductDimensionDto;

import java.util.UUID;

@Value
@Builder
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;

    Boolean fragile;

    @NotNull @Valid
    ProductDimensionDto dimension;

    @Positive
    Double weight;
}
