package panntod.core.learn_springboot.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler untuk seluruh aplikasi.
 * <p>
 * Semua exception yang dilempar dari controller
 * akan ditangani di sini agar response API konsisten.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler untuk error validasi (misalnya @Valid gagal).
     * Akan mengembalikan response 400 dengan detail field error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "Invalid value",
                        (msg1, msg2) -> msg1 // kalau ada duplicate field, ambil yang pertama
                ));

        return ResponseEntity.badRequest()
                .body(new ApiError("VALIDATION_ERROR", errors));
    }

    /**
     * Handler default untuk RuntimeException.
     * Akan mengembalikan response 500 dengan pesan error umum.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", ex.getMessage()));
    }
}
