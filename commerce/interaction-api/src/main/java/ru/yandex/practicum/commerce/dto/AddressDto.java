package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
