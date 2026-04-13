package ru.yandex.practicum.commerce.shoppingstore.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.model.Product;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        return Product.builder()
                .productId(productDto.getProductId())
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }
}