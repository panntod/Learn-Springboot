package panntod.core.learn_springboot.exceptions;

import java.util.Map;

/**
 * DTO untuk response error API.
 */
public class ApiError {
    private String code;           // kode error (misalnya VALIDATION_ERROR, INTERNAL_ERROR)
    private String message;        // pesan umum error
    private Map<String, String> errors; // detail field errors (opsional)

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError(String code, Map<String, String> errors) {
        this.code = code;
        this.errors = errors;
    }

    // Getter & Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
}
