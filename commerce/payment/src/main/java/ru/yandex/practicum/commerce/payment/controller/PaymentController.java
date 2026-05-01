package ru.yandex.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
@Validated
public class PaymentController {
    @PostMapping
    public PaymentDto payOrder(@RequestBody @Valid OrderDto request) {
        return null;
    }

    @PostMapping("/totalCost")
    public double calculateTotalCost(@RequestBody @Valid OrderDto request) {
        return 0.0;
    }

    @PostMapping("/refund")
    public void refundOrder(@RequestBody @Valid @NotNull UUID paymentId) {
    }

    @PostMapping("/productCost")
    public double calculateProductCost(@RequestBody @Valid OrderDto request) {
        return 0.0;
    }

    @PostMapping("/failed")
    public void paymentFailed(@RequestBody @Valid @NotNull UUID paymentId) {
    }
}
