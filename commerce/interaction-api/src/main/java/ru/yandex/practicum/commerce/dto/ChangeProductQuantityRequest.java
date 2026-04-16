package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@ToString
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;

    @Positive
    Integer newQuantity;
}
