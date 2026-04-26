package ru.yandex.practicum.commerce.error;

public class ItemOutOfStockException extends RuntimeException {
    public ItemOutOfStockException(String message) {
        super(message);
    }
}
