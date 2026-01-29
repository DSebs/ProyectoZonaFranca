package com.tayronadev.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta de error detallada para la API.
 * Incluye información adicional como errores de validación.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private final boolean success;
    private final String message;
    private final String error;
    private final Integer status;
    private final String path;
    private final List<FieldError> fieldErrors;
    private final LocalDateTime timestamp;
    
    /**
     * Representa un error de validación en un campo específico
     */
    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
        private final Object rejectedValue;
    }
    
    public static ErrorResponse of(String message, String error, Integer status, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .error(error)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponse withValidationErrors(String message, List<FieldError> fieldErrors, 
                                                      Integer status, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .error("Validation Error")
                .status(status)
                .path(path)
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
