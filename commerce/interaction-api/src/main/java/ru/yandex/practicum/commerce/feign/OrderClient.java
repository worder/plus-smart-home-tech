package ru.yandex.practicum.commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
//    @GetMapping
//    Collection<OrderDto> getOrder(@RequestParam String username);
//
//    @PutMapping
//    OrderDto createOrder(@RequestBody CreateNewOrderRequest request);
//
//    @PostMapping("/return")
//    OrderDto returnOrder(@RequestBody ReturnOrderRequest request);
//
    @PostMapping("/payment")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);
//
//    @PostMapping("/completed")
//    OrderDto completeOrder(@RequestBody UUID orderId);
//
//    @PostMapping("/calculate/total")
//    OrderDto calculateTotal(@RequestBody UUID orderId);
//
//    @PostMapping("/calculate/delivery")
//    OrderDto calculateDelivery(@RequestBody UUID orderId);
//
    @PostMapping("/calculate/assembly")
    OrderDto assemblySuccess(@RequestBody UUID orderId);

//    @PostMapping("/calculate/assembly/failed")
//    OrderDto assemblyFailed(@RequestBody UUID orderId);
}
