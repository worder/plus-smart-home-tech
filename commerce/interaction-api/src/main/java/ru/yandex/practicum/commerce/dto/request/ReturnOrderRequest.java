package ru.yandex.practicum.commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
public class ReturnOrderRequest {
    @NotNull
    UUID orderId;

    @NotNull
    Map<UUID, Integer> products;
}
