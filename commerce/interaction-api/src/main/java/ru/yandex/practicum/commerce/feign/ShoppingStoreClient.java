package ru.yandex.practicum.commerce.feign;

import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.enums.ProductCategory;
import ru.yandex.practicum.commerce.dto.request.UpdateProductQuantityRequest;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping
    Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ProductDto addProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean deleteProduct(@RequestBody @NotBlank UUID productId);

    @PostMapping("/quantityState")
    boolean updateQuantity(UpdateProductQuantityRequest request);

    @GetMapping("/{productId}")
    Optional<ProductDto> getProduct(@PathVariable UUID productId);
}
