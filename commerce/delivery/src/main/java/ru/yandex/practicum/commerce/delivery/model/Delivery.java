package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.dto.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    private UUID orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;
}
