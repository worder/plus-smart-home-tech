package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void putNewProduct(NewProductInWarehouseRequest request);

    void addProduct(AddProductToWarehouseRequest request);

    BookedProductsDto checkBookedProducts(ShoppingCartDto shoppingCartDto);

    AddressDto getAddress();

    void shipToDelivery(ShippedToDeliveryRequest request);

    void returnToWarehouse(Map<UUID, Integer> products);

    void assemblyOrder(AssemblyProductsForOrderRequest request);
}
