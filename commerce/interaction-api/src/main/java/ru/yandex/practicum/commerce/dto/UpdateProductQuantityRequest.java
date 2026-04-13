package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UpdateProductQuantityRequest {
    @NotBlank
    UUID productId;

    @NotNull
    QuantityState quantityState;
}
