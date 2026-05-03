package ru.yandex.practicum.commerce.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto notFound(ItemNotFoundException e) {
        log.error(e.getMessage());
        logStackTrace(e);

        return mapToDto(e, HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto badRequest(RuntimeException e) {
        log.error(e.getMessage());
        logStackTrace(e);

        return mapToDto(e, HttpStatus.BAD_REQUEST, "Bad Request");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto internalError(Exception e) {
        log.error(e.getMessage());
        logStackTrace(e);

        return mapToDto(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    protected void logStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log.error(sw.toString());
    }

    protected static ErrorResponseDto mapToDto(Exception e, HttpStatus status, String message) {
        return ErrorResponseDto.builder()
                .cause(e.getCause())
                .stackTrace(Arrays.asList(e.getStackTrace()))
                .httpStatus(status.toString())
                .userMessage(e.getMessage())
                .message(message)
                .suppressed(Arrays.asList(e.getSuppressed()))
                .build();
    }
}
