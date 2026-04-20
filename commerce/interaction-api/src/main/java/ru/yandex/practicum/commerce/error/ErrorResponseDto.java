package ru.yandex.practicum.commerce.error;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ErrorResponseDto {
    Throwable cause;
    List<StackTraceElement> stackTrace;
    String httpStatus;
    String userMessage;
    String message;
    List<Throwable> suppressed;
    String localizedMessage;
}
