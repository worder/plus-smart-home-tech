package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.enums.DeliveryState;

import java.util.UUID;

@Value
@Builder
@ToString
public class DeliveryDto {
    @NotNull
    UUID deliveryId;
    AddressDto fromAddress;
    AddressDto toAddress;
    UUID orderId;
    DeliveryState deliveryState;
}
