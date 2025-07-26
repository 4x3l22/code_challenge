package com.ms.prueba.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno", ex.getMessage());
    }

    // 401 - Unauthorized
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Acceso no autorizado", ex.getMessage());
    }

    // 422 - Unprocessable Entity (por ejemplo, errores de validación de DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "error", "Datos inválidos",
                        "details", errors
                ));
    }

    // 400 - Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Parámetro inválido", ex.getMessage());
    }

    // Método genérico para construir respuestas
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}
