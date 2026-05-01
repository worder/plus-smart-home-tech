package ru.yandex.practicum.commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto);

//    @PostMapping("/successful")
//    void deliverySuccessful(@RequestBody UUID orderId);

//    @PostMapping("/picked")
//    void deliveryPicked(@RequestBody UUID orderId);

//    @PostMapping("/failed")
//    void deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/cost")
    Double deliveryCost(@RequestBody OrderDto orderDto);
}
