package ru.yandex.practicum.commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.AddressDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;

@Value
@Builder
@ToString
public class CreateNewOrderRequest {
    @NotNull
    ShoppingCartDto shoppingCart;

    @NotNull
    AddressDto deliveryAddress;
}
