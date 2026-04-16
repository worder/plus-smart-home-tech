package ru.yandex.practicum.commerce.shoppingcart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {
    final private ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@Valid @RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@Valid @RequestParam @NotBlank String username,
                                                    @Valid @RequestBody Map<@NotNull UUID, @Positive Integer> products) {
        return shoppingCartService.addProducts(username, products);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@Valid @RequestParam @NotEmpty String username) {
        shoppingCartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromShoppingCart(@Valid @RequestParam @NotEmpty String username,
                                                          @Valid @RequestParam(required = false) List<@NotNull UUID> products) {
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInUserCart(@Valid @RequestParam @NotBlank String username,
                                                           @Valid @RequestBody ChangeProductQuantityRequest request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }
}
