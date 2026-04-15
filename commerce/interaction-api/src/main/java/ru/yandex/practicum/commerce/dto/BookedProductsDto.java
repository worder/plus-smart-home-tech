package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookedProductsDto {
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
}
