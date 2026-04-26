package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.dto.*;

public interface WarehouseService {
    void putNewProduct(NewProductInWarehouseRequest request);

    void addProduct(AddProductToWarehouseRequest request);

    BookedProductsDto checkBookedProducts(ShoppingCartDto shoppingCartDto);

    WarehouseAddressDto getAddress();
}
