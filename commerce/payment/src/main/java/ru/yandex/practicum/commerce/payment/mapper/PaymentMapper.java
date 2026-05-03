package ru.yandex.practicum.commerce.payment.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.Payment;

@UtilityClass
public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .productTotal(payment.getProductTotal())
                .feeTotal(payment.getFeeTotal())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }
}
