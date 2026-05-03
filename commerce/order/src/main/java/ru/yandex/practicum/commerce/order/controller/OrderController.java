package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.request.ReturnOrderRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public Page<OrderDto> getOrders(@RequestParam @NotBlank String username, Pageable pageable) {
        return orderService.findOrders(username, pageable);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody @Valid ReturnOrderRequest request) {
        return orderService.returnOrder(request);
    }

    @PostMapping("/payment")
    public OrderDto paymentSuccess(@RequestBody @NotNull UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody @NotNull UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliverOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.deliverySuccess(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody @NotNull UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.completed(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateTotalPrice(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateDeliveryPrice(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblySuccess(@RequestBody @NotNull UUID orderId) {
        return orderService.assemblySuccess(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody @NotNull UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}
