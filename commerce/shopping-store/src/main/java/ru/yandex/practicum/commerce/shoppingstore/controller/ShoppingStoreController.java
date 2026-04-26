package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.UpdateProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public Page<ProductDto> getProducts(@Valid @RequestParam ProductCategory category, Pageable pageable) {
        return shoppingStoreService.findProducts(category, pageable);
    }

    @PutMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        return shoppingStoreService.addProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return  shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@Valid @RequestBody @NotNull UUID productId) {
        shoppingStoreService.deleteProduct(productId);
        return true;
    }

    @PostMapping("/quantityState")
    public boolean updateQuantity(@Valid UpdateProductQuantityRequest request) {
        shoppingStoreService.updateProductQuantity(request);
        return true;
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}
