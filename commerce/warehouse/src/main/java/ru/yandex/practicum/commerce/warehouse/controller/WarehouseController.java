package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
@Validated
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    public void putNewProductInWarehouse(@RequestBody NewProductInWarehouseRequest request) {
        warehouseService.putNewProduct(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkBookedProducts(@RequestBody ShoppingCartDto cart) {
        return warehouseService.checkBookedProducts(cart);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @GetMapping
    public WarehouseAddressDto getWarehouseAddress() {
        return warehouseService.getAddress();
    }
}
