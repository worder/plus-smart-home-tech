package ru.yandex.practicum.commerce.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.order.model.Order;

@UtilityClass
public class OrderMapper {
    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }
}
