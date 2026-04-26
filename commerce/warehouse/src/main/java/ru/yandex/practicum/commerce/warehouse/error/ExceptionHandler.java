package ru.yandex.practicum.commerce.warehouse.error;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.error.GlobalExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends GlobalExceptionHandler {
}
