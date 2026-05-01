package ru.yandex.practicum.commerce.warehouse.mapper;

import ru.yandex.practicum.commerce.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

public class WarehouseProductMapper {
    public static WarehouseProduct toWarehouseProductEntity(NewProductInWarehouseRequest request) {
        return WarehouseProduct.builder()
                .productId(request.getProductId())
                .fragile(request.getFragile())
                .weight(request.getWeight())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .build();
    }
}
