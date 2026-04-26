package ru.yandex.practicum.commerce.error;

public class ItemExistsException extends RuntimeException {
    public ItemExistsException(String message) {
        super(message);
    }
}
