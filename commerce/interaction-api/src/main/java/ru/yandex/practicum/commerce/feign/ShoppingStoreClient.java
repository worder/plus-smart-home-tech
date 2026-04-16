package ru.yandex.practicum.commerce.feign;


import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.UpdateProductQuantityRequest;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    public ProductDto addProduct(@RequestBody ProductDto productDto);

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@RequestBody @NotBlank UUID productId);

    @PostMapping("/quantityState")
    public boolean updateQuantity(UpdateProductQuantityRequest request);

    @GetMapping("/{productId}")
    public Optional<ProductDto> getProduct(@PathVariable UUID productId);
}
