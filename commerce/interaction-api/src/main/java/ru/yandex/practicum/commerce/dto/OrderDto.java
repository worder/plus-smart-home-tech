package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.enums.OrderState;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class OrderDto {
    UUID orderId;
    UUID shoppingCartId;
    Map<UUID, Integer> products;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
}
