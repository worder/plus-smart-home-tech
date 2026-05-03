package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.enums.DeliveryState;

import java.util.UUID;

@Value
@Builder
@ToString
public class DeliveryDto {
    UUID deliveryId;
    AddressDto fromAddress;
    AddressDto toAddress;
    UUID orderId;
    DeliveryState deliveryState;
}
