package ru.yandex.practicum.commerce.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.feign.ShoppingStoreClient;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseProductRepository repository;
    private final ShoppingStoreClient shoppingStoreClient;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    @Override
    public void putNewProduct(NewProductInWarehouseRequest request) {
        if (repository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("Product with id " + request.getProductId() + " already exists");
        }
        repository.save(WarehouseProductMapper.toWarehouseProductEntity(request));
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct product = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product with id " + request.getProductId() + " does not exists"));
        product.setQuantity(request.getQuantity() + request.getQuantity());
        repository.save(product);

        updateStoreQuantity(product.getProductId(), product.getQuantity());
    }

    @Override
    public BookedProductsDto checkBookedProducts(ShoppingCartDto shoppingCartDto) {
        Set<UUID> productIds = shoppingCartDto.getProducts().keySet();

        Map<UUID, WarehouseProduct> products = repository.findAllByProductId(productIds)
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, p -> p));

        for (Map.Entry<UUID, Integer> entry : shoppingCartDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer requiredQuantity = entry.getValue();
            WarehouseProduct warehouseProduct = products.get(productId);

            if (!products.containsKey(productId)) {
                throw new IllegalArgumentException("Product with id " + productId + " does not exists");
            }
            if (warehouseProduct.getQuantity() < requiredQuantity) {
                throw new IllegalArgumentException("Product with id " + productId + " has less than required quantity");
            }
        }

        Double totalWeight = products.values().stream()
                .reduce(0.0, (sum, p) ->
                        sum + (p.getWeight() * p.getQuantity()), Double::sum);

        Double totalVolume = products.values().stream()
                .reduce(0.0, (sum, p) ->
                        sum + (p.getWidth() * p.getDepth() * p.getLength() * p.getQuantity()), Double::sum);

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

    private void updateStoreQuantity(UUID productId, Integer quantity) {
        QuantityState quantityState;
        if (quantity == 0) {
            quantityState = QuantityState.ENDED;
        } else if (quantity < 10) {
            quantityState = QuantityState.ENOUGH;
        } else if (quantity < 100) {
            quantityState = QuantityState.FEW;
        } else {
            quantityState = QuantityState.MANY;
        }

        shoppingStoreClient.updateQuantity(new UpdateProductQuantityRequest(productId, quantityState));
    }
}
