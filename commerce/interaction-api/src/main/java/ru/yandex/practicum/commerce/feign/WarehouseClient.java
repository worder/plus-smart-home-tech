package ru.yandex.practicum.commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {
    @PutMapping
    void putNewProductInWarehouse(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkBookedProducts(@RequestBody ShoppingCartDto cart);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping
    AddressDto getWarehouseAddress();

    @PostMapping("/shipped")
    void shipToDelivery(ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Integer> request);

    @PostMapping("/assembly")
    void assemblyOrder(@RequestBody AssemblyProductsForOrderRequest request);
}
