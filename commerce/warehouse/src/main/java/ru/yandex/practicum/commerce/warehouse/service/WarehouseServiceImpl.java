package ru.yandex.practicum.commerce.warehouse.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.error.ItemOutOfStockException;
import ru.yandex.practicum.commerce.error.ItemExistsException;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseProductRepository repository;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    @Override
    public void putNewProduct(NewProductInWarehouseRequest request) {
        if (repository.existsByProductId(request.getProductId())) {
            throw new ItemExistsException("Product with id " + request.getProductId() + " already exists");
        }
        repository.save(WarehouseProductMapper.toWarehouseProductEntity(request));
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct product = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Product with id " + request.getProductId() + " does not exists"));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
    }

    @Override
    public BookedProductsDto checkBookedProducts(ShoppingCartDto shoppingCartDto) {
        Set<UUID> productIds = shoppingCartDto.getProducts().keySet();
        log.info("> checking booked products for cart {}", shoppingCartDto);

        Map<UUID, WarehouseProduct> products = repository.findAllByProductIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, p -> p));

        log.info("> found products: {}", products.values());

        for (Map.Entry<UUID, Integer> entry : shoppingCartDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer requiredQuantity = entry.getValue();
            WarehouseProduct warehouseProduct = products.get(productId);

            if (!products.containsKey(productId)) {
                throw new ItemNotFoundException("Product with id " + productId + " does not exists");
            }
            if (warehouseProduct.getQuantity() < requiredQuantity) {
                throw new ItemOutOfStockException("Product with id " + productId + " has less than required quantity");
            }
        }

        Double totalWeight = products.values().stream()
                .reduce(0.0, (sum, p) ->
                        sum + (p.getWeight() * p.getQuantity()), Double::sum);

        Double totalVolume = products.values().stream()
                .reduce(0.0, (sum, p) ->
                        sum + (p.getWidth() * p.getDepth() * p.getHeight() * p.getQuantity()), Double::sum);

        Boolean fragile = products.values().stream().anyMatch(WarehouseProduct::getFragile);

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragile)
                .build();
    }

    @Override
    public WarehouseAddressDto getAddress() {
        return WarehouseAddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }
}
