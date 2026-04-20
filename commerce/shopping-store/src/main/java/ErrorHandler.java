import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.error.GlobalExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends GlobalExceptionHandler {
}
