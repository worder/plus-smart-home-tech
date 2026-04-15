package ru.yandex.practicum.commerce.shoppingcart.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
@Validated
public class ShoppingCartController {
    final private ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam @NotBlank String username,
                                                    @RequestBody Map<@NotNull UUID, @Positive Integer> products) {
        return shoppingCartService.addProducts(username, products);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam @NotEmpty String username) {
        shoppingCartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromShoppingCart(@RequestParam @NotEmpty String username,
                                          @RequestParam @NotEmpty List<@NotNull UUID> products) {
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInUserCart(@RequestParam @NotBlank String username,
                                                           @RequestBody ChangeProductQuantityRequest request) {
        return  shoppingCartService.changeProductQuantity(username, request);
    }
}
