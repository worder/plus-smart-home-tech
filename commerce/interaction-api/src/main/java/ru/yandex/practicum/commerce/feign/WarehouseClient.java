package ru.yandex.practicum.commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.*;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {
    @PutMapping
    public void putNewProductInWarehouse(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    public BookedProductsDto checkBookedProducts(@RequestBody ShoppingCartDto cart);

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping
    public WarehouseAddressDto getWarehouseAddress();
}
