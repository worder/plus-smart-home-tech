package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.ProductCategory;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.UpdateProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.model.Product;
import ru.yandex.practicum.commerce.shoppingstore.repository.ShoppingStoreRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = shoppingStoreRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public Optional<ProductDto> getProduct(String productId) {
        UUID id = UUID.fromString(productId);
        return shoppingStoreRepository.findById(id)
                .map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> findProducts(ProductCategory category, Pageable pageable) {
        return shoppingStoreRepository.findByProductCategory(category, pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product updatedProduct = shoppingStoreRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        UUID id = UUID.fromString(productId);
        shoppingStoreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateProductQuantity(UpdateProductQuantityRequest request) {
        Product product = shoppingStoreRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with id: " + request.getProductId()));
        product.setQuantityState(request.getQuantityState());
        shoppingStoreRepository.save(product);
    }
}
