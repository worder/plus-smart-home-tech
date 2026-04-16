package ru.yandex.practicum.commerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.dto.ShoppingCartState;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shoppingCartId;

    private String username;

    @Enumerated(EnumType.STRING)
    private ShoppingCartState state;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}
