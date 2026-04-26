package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    public void putNewProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest request) {
        warehouseService.putNewProduct(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkBookedProducts(@Valid @RequestBody ShoppingCartDto cart) {
        return warehouseService.checkBookedProducts(cart);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @GetMapping("/address")
    public WarehouseAddressDto getWarehouseAddress() {
        return warehouseService.getAddress();
    }
}
