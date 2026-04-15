package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.UpdateProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return shoppingStoreService.findProducts(category, pageable);
    }

    @PutMapping
    public ProductDto addProduct(@RequestBody @Valid ProductDto productDto) {
        return shoppingStoreService.addProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        return  shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@RequestBody @NotBlank UUID productId) {
        shoppingStoreService.deleteProduct(productId);
        return true;
    }

    @PostMapping("/quantityState")
    public boolean updateQuantity(@Valid UpdateProductQuantityRequest request) {
        shoppingStoreService.updateProductQuantity(request);
        return true;
    }

    @GetMapping("/{productId}")
    public Optional<ProductDto> getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}
