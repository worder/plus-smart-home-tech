package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductQuantityRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
