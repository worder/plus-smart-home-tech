package ru.yandex.practicum.commerce.dto;

import java.util.UUID;

public class PaymentDto {
    UUID paymentId;
    double totalPayment;
    double deliveryTotal;
    double feeTotal;
}
