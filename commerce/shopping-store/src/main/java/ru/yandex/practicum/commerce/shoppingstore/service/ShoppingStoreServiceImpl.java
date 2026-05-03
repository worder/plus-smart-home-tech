package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.dto.enums.ProductCategory;
import ru.yandex.practicum.commerce.dto.enums.ProductState;
import ru.yandex.practicum.commerce.dto.request.UpdateProductQuantityRequest;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.model.Product;
import ru.yandex.practicum.commerce.shoppingstore.repository.ShoppingStoreRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = ProductMapper.toEntity(productDto);
        Product savedProduct = shoppingStoreRepository.save(product);
        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return shoppingStoreRepository.findByProductId(productId)
                .map(ProductMapper::toDto)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public Page<ProductDto> findProducts(ProductCategory category, Pageable pageable) {
        return shoppingStoreRepository.findByProductCategory(category, pageable)
                .map(ProductMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        if (!shoppingStoreRepository.existsByProductId(productDto.getProductId())) {
            throw new ItemNotFoundException("Product not found with id: " + productDto.getProductId());
        }

        Product updatedProduct = shoppingStoreRepository.save(ProductMapper.toEntity(productDto));
        return ProductMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product currentProduct = shoppingStoreRepository.findByProductId(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + productId));

        currentProduct.setProductState(ProductState.DEACTIVATE);
        shoppingStoreRepository.save(currentProduct);
    }

    @Override
    @Transactional
    public void updateProductQuantity(UpdateProductQuantityRequest request) {
        Product product = shoppingStoreRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Product not found with id: " + request.getProductId()));

        product.setQuantityState(request.getQuantityState());
        shoppingStoreRepository.save(product);
    }
}
