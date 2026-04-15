package ru.yandex.practicum.commerce.shoppingcart.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartState;
import ru.yandex.practicum.commerce.feign.WarehouseClient;
import ru.yandex.practicum.commerce.shoppingcart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shoppingcart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shoppingcart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return ShoppingCartMapper.toDto(getOrCreateShoppingCart(username));
    }

    @Override
    public void deactivateCart(String username) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("ShoppingCart not found"));

        cart.setState(ShoppingCartState.DEACTIVATE);
        shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);

        cart.getProducts().putAll(products);

        warehouseClient.checkBookedProducts(ShoppingCartMapper.toDto(cart));

        shoppingCartRepository.save(cart);
        return ShoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> products) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);
        if (cart.getProducts().isEmpty()) {
            throw new IllegalArgumentException("ShoppingCart is empty");
        }
        for (UUID id : products) {
            cart.getProducts().remove(id);
        }
        shoppingCartRepository.save(cart);
        return ShoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = this.getOrCreateShoppingCart(username);
        if (cart.getProducts().containsKey(request.getProductId())) {
            cart.getProducts().put(request.getProductId(), request.getNewQuantity());
            shoppingCartRepository.save(cart);
            return ShoppingCartMapper.toDto(cart);
        }

        throw new IllegalArgumentException("Product not found");
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        return shoppingCartRepository.findByUsernameAndState(username, ShoppingCartState.ACTIVE)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .username(username)
                            .state(ShoppingCartState.ACTIVE)
                            .build();
                    return shoppingCartRepository.save(newCart);
                });
    }
}
