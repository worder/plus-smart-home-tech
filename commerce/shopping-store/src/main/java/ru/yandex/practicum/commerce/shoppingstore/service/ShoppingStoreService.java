package ru.yandex.practicum.commerce.shoppingstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.dto.enums.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.request.UpdateProductQuantityRequest;

import java.util.UUID;

public interface ShoppingStoreService {
    ProductDto addProduct(ProductDto productDto);

    ProductDto getProduct(UUID productId);

    Page<ProductDto> findProducts(ProductCategory category, Pageable pageable);

    ProductDto updateProduct(ProductDto productDto);

    void deleteProduct(UUID productId);

    void updateProductQuantity(UpdateProductQuantityRequest request);
}
