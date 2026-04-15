package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WarehouseAddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
