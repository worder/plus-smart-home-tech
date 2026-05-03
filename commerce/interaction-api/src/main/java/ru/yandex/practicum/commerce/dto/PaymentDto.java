package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.enums.PaymentStatus;

import java.util.UUID;

@Value
@Builder
public class PaymentDto {
    UUID paymentId;
    double totalPayment;
    double deliveryTotal;
    double productTotal;
    double feeTotal;
    PaymentStatus paymentStatus;
}
