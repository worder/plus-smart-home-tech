package ru.yandex.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@AllArgsConstructor
@Validated
public class DeliveryController {
    @PutMapping
    public DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        return null;
    }

    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody @NotNull UUID orderId) {
    }

    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody @NotNull UUID orderId) {
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody @NotNull UUID orderId) {
    }

    @PostMapping("/cost")
    public Double deliveryCost(@RequestBody @NotNull OrderDto orderDto) {
        return null;
    }

}
