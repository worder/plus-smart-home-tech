package ru.yandex.practicum.commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.dto.enums.QuantityState;

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
