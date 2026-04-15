package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDimensionDto {
    @Positive
    Double width;

    @Positive
    Double length;

    @Positive
    Double depth;
}
