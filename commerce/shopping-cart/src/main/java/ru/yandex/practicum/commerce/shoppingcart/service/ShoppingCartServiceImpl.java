package ru.yandex.practicum.commerce.shoppingcart.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartState;
import ru.yandex.practicum.commerce.error.ItemNotFoundException;
import ru.yandex.practicum.commerce.feign.WarehouseClient;
import ru.yandex.practicum.commerce.shoppingcart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shoppingcart.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.info("> get shopping cart for {}", username);
        ShoppingCart cart =  getOrCreateShoppingCart(username);
        log.info("> returning cart {}", cart);
        return ShoppingCartMapper.toDto(cart);
    }

    @Override
    public void deactivateCart(String username) {
        ShoppingCart cart = shoppingCartRepository.findByUsernameAndState(username, ShoppingCartState.ACTIVE)
                .orElseThrow(() -> new ItemNotFoundException("ShoppingCart not found"));

        log.info("> deactivating cart {}; for username {}", cart, username);
        cart.setState(ShoppingCartState.DEACTIVATE);
        shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);
        cart.getProducts().putAll(products);
        log.info("> booking products {} for username {} to cart {}", products, username, cart);

        BookedProductsDto bookedProducts = warehouseClient.checkBookedProducts(ShoppingCartMapper.toDto(cart));
        log.info("> booking check success, saving cart: {}", bookedProducts);

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        return ShoppingCartMapper.toDto(updatedCart);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> products) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);
        if (products != null && !products.isEmpty()) {
            log.info("> removing products {} from cart {} for username {}", products, cart, products);

            for (UUID id : products) {
                cart.getProducts().remove(id);
            }
            shoppingCartRepository.save(cart);
        } else {
            log.info("> no products to remove for username {}", username);
        }

        return ShoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);
        log.info("> changing quantity of product {} in cart {} for username {}", request, cart, username);

        if (cart.getProducts().containsKey(request.getProductId())) {
            cart.getProducts().put(request.getProductId(), request.getNewQuantity());
            shoppingCartRepository.save(cart);
            return ShoppingCartMapper.toDto(cart);
        }

        throw new ItemNotFoundException("Product not found");
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        return shoppingCartRepository.findByUsernameAndState(username, ShoppingCartState.ACTIVE)
                .orElseGet(() -> {
                    log.info("> shopping card not found for user {}, creating new",  username);
                    ShoppingCart newCart = ShoppingCart.builder()
                            .username(username)
                            .state(ShoppingCartState.ACTIVE)
                            .products(new HashMap<>())
                            .build();
                    return shoppingCartRepository.save(newCart);
                });
    }
}
