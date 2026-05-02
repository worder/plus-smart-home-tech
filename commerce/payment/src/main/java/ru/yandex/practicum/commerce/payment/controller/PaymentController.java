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
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid OrderDto request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/totalCost")
    public double calculateTotalCost(@RequestBody @Valid OrderDto request) {
        return paymentService.calculateTotalCost(request);
    }

    @PostMapping("/productCost")
    public double calculateProductCost(@RequestBody @Valid OrderDto request) {
        return paymentService.calculateProductCost(request);
    }

    @PostMapping("/success")
    public void refundOrder(@RequestBody @Valid @NotNull UUID paymentId) {
        paymentService.paymentSuccess(paymentId);
    }

    @PostMapping("/failed")
    public void paymentFailed(@RequestBody @Valid @NotNull UUID paymentId) {
        paymentService.paymentFailed(paymentId);
    }
}
