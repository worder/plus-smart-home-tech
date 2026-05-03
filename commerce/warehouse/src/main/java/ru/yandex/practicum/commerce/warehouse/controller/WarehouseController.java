package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
@Validated
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
    public AddressDto getWarehouseAddress() {
        return warehouseService.getAddress();
    }

    @PostMapping("/shipped")
    public void shipToDelivery(@Valid ShippedToDeliveryRequest request) {
        warehouseService.shipToDelivery(request);
    }

    @PostMapping("/return")
    public void returnProducts(@RequestBody @NotEmpty Map<@NotNull UUID, @Positive Integer> request) {
        warehouseService.returnToWarehouse(request);
    }

    @PostMapping("/assembly")
    BookedProductsDto assemblyOrder(@Valid @RequestBody AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyOrder(request);
    }


}
