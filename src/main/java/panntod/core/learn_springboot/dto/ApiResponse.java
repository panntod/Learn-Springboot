package panntod.core.learn_springboot.dto;

import java.util.Optional;

/**
 * Response umum untuk API.
 * @param <T> tipe data payload (misalnya ProductDto, List<ProductDto>, dll)
 */
public record ApiResponse<T>(
        String status,
        String message,
        Optional<T> data
) {
    // Convenience constructor tanpa data (biar lebih ringkas kalau nggak ada payload)
    public ApiResponse(String status, String message) {
        this(status, message, Optional.empty());
    }
}
