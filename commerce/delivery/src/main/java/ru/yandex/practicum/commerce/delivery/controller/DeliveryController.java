package ru.yandex.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@AllArgsConstructor
@Validated
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliverySuccess(orderId);
    }

    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliveryPicked(orderId);
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliveryFail(orderId);
    }

    @PostMapping("/cost")
    public Double deliveryCost(@RequestBody @NotNull OrderDto orderDto) {
        return deliveryService.calculateDeliveryCost(orderDto);
    }

}
