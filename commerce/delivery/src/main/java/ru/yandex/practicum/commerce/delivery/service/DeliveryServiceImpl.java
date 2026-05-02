package ru.yandex.practicum.commerce.delivery.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Address;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.enums.DeliveryState;
import ru.yandex.practicum.commerce.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.feign.OrderClient;
import ru.yandex.practicum.commerce.feign.WarehouseClient;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;

    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private final static double BASE_COST = 5.0;
    private final static double FRAGILE_MULTIPLIER = 0.2;
    private final static double WEIGHT_MULTIPLIER = 0.3;
    private final static double VOLUME_MULTIPLIER = 0.2;
    private final static double DISTANCE_MULTIPLIER = 0.2;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.toDelivery(deliveryDto);
        delivery.setState(DeliveryState.CREATED);
        delivery = deliveryRepository.save(delivery);
        log.info("Created delivery: {} from request: {}", delivery, deliveryDto);

        return DeliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    public void deliverySuccess(UUID deliveryId) {
        Delivery delivery = this.getDelivery(deliveryId);
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.delivery(delivery.getOrderId());
        log.info("Delivery complete successfully: {} for order: {}", deliveryId, delivery.getOrderId());
    }

    @Override
    public void deliveryFail(UUID deliveryId) {
        Delivery delivery = this.getDelivery(deliveryId);
        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.deliveryFailed(delivery.getOrderId());
        log.info("Delivery failed: {} for order: {}", deliveryId, delivery.getOrderId());
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = this.getDelivery(deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        orderClient.assemblySuccess(delivery.getOrderId());
        warehouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .deliveryId(deliveryId)
                .orderId(delivery.getOrderId())
                .build());
        log.info("Delivery in progress: {} for order: {}", deliveryId, delivery.getOrderId());
    }

    @Override
    public double calculateDeliveryCost(OrderDto order) {
        Delivery delivery = this.getDelivery(order.getDeliveryId());

        Address warehouseAddress = delivery.getFromAddress();
        Address clientAddress = delivery.getToAddress();

        double totalCost = BASE_COST;

        // calculate base cost
        final double baseCostMultiplier = switch (warehouseAddress.getCity()) {
            case "ADDRESS_1" -> 1.0;
            case "ADDRESS_2" -> 2.0;
            default -> 1.0;
        };

        totalCost += BASE_COST * baseCostMultiplier;

        // fragile flag
        if (order.isFragile()) {
            totalCost += totalCost * FRAGILE_MULTIPLIER;
        }

        // weight
        totalCost += order.getDeliveryWeight() * WEIGHT_MULTIPLIER;

        // volume
        totalCost += order.getDeliveryVolume() * VOLUME_MULTIPLIER;

        // long distance
        if (!clientAddress.getStreet().equals(warehouseAddress.getStreet())) {
            totalCost += totalCost * DISTANCE_MULTIPLIER;
        }

        log.info("Calculate delivery cost: {} for order: {}", totalCost, order.getOrderId());

        return totalCost;
    }

    private Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ItemNotFoundException("Delivery with id " + deliveryId + " Not Found"));
    }
}
