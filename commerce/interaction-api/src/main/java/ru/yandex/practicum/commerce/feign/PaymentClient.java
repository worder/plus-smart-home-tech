package ru.yandex.practicum.commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {
    @PostMapping
    PaymentDto payment(@RequestBody OrderDto request);

    @PostMapping("/totalCost")
    double getTotalCost(@RequestBody OrderDto request);

    @PostMapping("/productCost")
    double productCost(@RequestBody OrderDto request);
}
