package ru.yandex.practicum.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class AssemblyProductsForOrderRequest {
    @NotEmpty
    Map<@NotNull UUID, @Positive Integer> products;

    @NotNull
    UUID orderId;
}
