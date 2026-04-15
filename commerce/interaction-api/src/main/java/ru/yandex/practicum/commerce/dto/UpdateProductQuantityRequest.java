package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class UpdateProductQuantityRequest {
    @NotNull
    UUID productId;

    @NotNull
    QuantityState quantityState;
}
