package ru.yandex.practicum.commerce.warehouse.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.AddressDto;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.error.ItemExistsException;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.error.ItemOutOfStockException;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.OrderBookingsRepository;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseProductRepository repository;
    private final OrderBookingsRepository orderBookingsRepository;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    @Transactional
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
        log.info("Checking booked products for cart {}", shoppingCartDto);
        return isProductQuantityAvailable(shoppingCartDto.getProducts());
    }

    @Transactional
    @Override
    public BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> products = request.getProducts();
        BookedProductsDto bookedProducts = isProductQuantityAvailable(products);

        OrderBooking booking = OrderBooking.builder()
                .orderId(request.getOrderId())
                .products(request.getProducts())
                .build();
        booking =  orderBookingsRepository.save(booking);
        log.info("Created order booking: {} from request: {}", booking, request);

        // reduce warehouse stock
        Collection<WarehouseProduct> warehouseStock = repository.findAllByProductIdIn(booking.getProducts().keySet());
        for (WarehouseProduct warehouseProduct : warehouseStock) {
            warehouseProduct
                    .setQuantity(warehouseProduct.getQuantity() - products.get(warehouseProduct.getProductId()));
        }

        return bookedProducts;
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        OrderBooking booking = orderBookingsRepository
                .findByOrderId(request.getOrderId())
                .orElseThrow(() ->
                        new ItemNotFoundException("Order booking not found for order: " + request.getOrderId()));

        booking.setDeliveryId(request.getDeliveryId());
        orderBookingsRepository.save(booking);
        log.info("Updated order booking status for id: {}, set deliveryId: {}",
                booking.getBookingId(), request.getDeliveryId());
    }

    @Override
    @Transactional
    public void returnToWarehouse(Map<UUID, Integer> products) {
        Map<UUID, WarehouseProduct> warehouseProducts = repository.findAllByProductIdIn(products.keySet())
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, p -> p));

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId =  entry.getKey();
            Integer returnedQuantity = entry.getValue();

            if (!warehouseProducts.containsKey(productId)) {
                throw new ItemNotFoundException("Product with id " + productId + " does not exists");
            }
            WarehouseProduct warehouseProduct = warehouseProducts.get(productId);
            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + returnedQuantity);
            log.info("Returned to warehouse, product id: {}, qty: +{}, new qty: {}",
                    productId, returnedQuantity, warehouseProduct.getQuantity());
        }
    }

    @Override
    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private BookedProductsDto isProductQuantityAvailable(Map<UUID, Integer> checkProducts) {
        Set<UUID> productIds = checkProducts.keySet();

        Map<UUID, WarehouseProduct> warehouseProducts = repository.findAllByProductIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, p -> p));

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        Boolean fragile = warehouseProducts.values().stream().anyMatch(WarehouseProduct::getFragile);

        for (Map.Entry<UUID, Integer> entry : checkProducts.entrySet()) {
            UUID productId = entry.getKey();
            Integer requiredQuantity = entry.getValue();
            WarehouseProduct warehouseProduct = warehouseProducts.get(productId);

            if (!warehouseProducts.containsKey(productId)) {
                throw new ItemNotFoundException("Product with id " + productId + " does not exists");
            }
            if (warehouseProduct.getQuantity() < requiredQuantity) {
                throw new ItemOutOfStockException("Product with id " + productId + " has less than required quantity");
            }

            totalWeight += warehouseProduct.getWeight() * requiredQuantity;
            totalVolume += warehouseProduct.getDepth()
                    * warehouseProduct.getHeight()
                    * warehouseProduct.getWidth()
                    * requiredQuantity;
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragile)
                .build();
    }
}
