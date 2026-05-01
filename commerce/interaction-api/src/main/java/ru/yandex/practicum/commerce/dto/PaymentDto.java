package ru.yandex.practicum.commerce.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class PaymentDto {
    UUID paymentId;
    double totalPayment;
    double deliveryTotal;
    double feeTotal;
}
