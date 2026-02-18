package com.tayronadev.api.common;

import com.tayronadev.dominio.auditoria.excepciones.RegistroNoEncontradoException;
import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import com.tayronadev.dominio.citas.excepciones.HorarioNoDisponibleException;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.InicioSesiónExcepcion;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la API.
 * Convierte las excepciones en respuestas HTTP apropiadas.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Maneja excepciones de validación de campos (Bean Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.warn("Error de validación en {}: {}", request.getRequestURI(), ex.getMessage());
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());
        
        var response = ErrorResponse.withValidationErrors(
                "Error de validación en los datos enviados",
                fieldErrors,
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja errores cuando no se encuentra una cita
     */
    @ExceptionHandler(CitaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCitaNoEncontrada(
            CitaNoEncontradaException ex,
            HttpServletRequest request) {
        
        log.warn("Cita no encontrada: {}", ex.getMessage());
        
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Cita No Encontrada",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Maneja errores de estado de cita inválido
     */
    @ExceptionHandler(EstadoCitaInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleEstadoCitaInvalido(
            EstadoCitaInvalidoException ex,
            HttpServletRequest request) {
        
        log.warn("Estado de cita inválido: {}", ex.getMessage());
        
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Estado de Cita Inválido",
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Maneja errores de horario no disponible
     */
    @ExceptionHandler(HorarioNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleHorarioNoDisponible(
            HorarioNoDisponibleException ex,
            HttpServletRequest request) {
        
        log.warn("Horario no disponible: {}", ex.getMessage());
        
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Horario No Disponible",
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Maneja errores cuando no se encuentra un registro de auditoría
     */
    @ExceptionHandler(RegistroNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRegistroNoEncontrado(
            RegistroNoEncontradoException ex,
            HttpServletRequest request) {
        
        log.warn("Registro de auditoría no encontrado: {}", ex.getMessage());
        
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Registro No Encontrado",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Correo inválido, no encontrado (login) o ya registrado (registro).
     */
    @ExceptionHandler(CorreoExcepcion.class)
    public ResponseEntity<ErrorResponse> handleCorreoExcepcion(
            CorreoExcepcion ex,
            HttpServletRequest request) {
        log.warn("Error de correo: {}", ex.getMessage());
        boolean correoYaRegistrado = ex.getMessage() != null
                && ex.getMessage().contains("registrado");
        if (correoYaRegistrado) {
            var response = ErrorResponse.of(
                    ex.getMessage(),
                    "Correo Ya Registrado",
                    HttpStatus.CONFLICT.value(),
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        var response = ErrorResponse.of(
                "Credenciales inválidas",
                "No Autorizado",
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Contraseña incorrecta en login.
     */
    @ExceptionHandler(ContraseñaExcepcion.class)
    public ResponseEntity<ErrorResponse> handleContraseñaExcepcion(
            ContraseñaExcepcion ex,
            HttpServletRequest request) {
        log.warn("Error de contraseña: {}", ex.getMessage());
        var response = ErrorResponse.of(
                "Credenciales inválidas",
                "No Autorizado",
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Cuenta inactiva (no puede iniciar sesión).
     */
    @ExceptionHandler(InicioSesiónExcepcion.class)
    public ResponseEntity<ErrorResponse> handleInicioSesionExcepcion(
            InicioSesiónExcepcion ex,
            HttpServletRequest request) {
        log.warn("Intento de login con cuenta inactiva: {}", ex.getMessage());
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Cuenta Inactiva",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Maneja errores de argumentos ilegales (validaciones de dominio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        log.warn("Argumento inválido: {}", ex.getMessage());
        
        var response = ErrorResponse.of(
                ex.getMessage(),
                "Datos Inválidos",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja errores de tipo de argumento incorrecto (ej: enum inválido)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        
        String message = String.format("El valor '%s' no es válido para el parámetro '%s'", 
                ex.getValue(), ex.getName());
        
        log.warn("Error de tipo: {}", message);
        
        var response = ErrorResponse.of(
                message,
                "Tipo de Parámetro Inválido",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja errores de deserialización JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        
        log.warn("Error al leer el mensaje: {}", ex.getMessage());
        
        String message = "El formato del cuerpo de la solicitud no es válido";
        
        // Intentar dar un mensaje más específico si es posible
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            String causeMessage = ex.getCause().getMessage();
            if (causeMessage.contains("Cannot deserialize value of type")) {
                message = "Valor inválido en el cuerpo de la solicitud. Verifique los tipos de datos.";
            }
        }
        
        var response = ErrorResponse.of(
                message,
                "Error de Formato",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja errores de NullPointerException para argumentos nulos requeridos
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(
            NullPointerException ex,
            HttpServletRequest request) {
        
        log.error("Error de valor nulo: {}", ex.getMessage(), ex);
        
        var response = ErrorResponse.of(
                ex.getMessage() != null ? ex.getMessage() : "Se recibió un valor nulo donde se esperaba un valor",
                "Valor Requerido Faltante",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja excepciones genéricas no capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Error no manejado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        var response = ErrorResponse.of(
                "Ha ocurrido un error interno. Por favor, intente más tarde.",
                "Error Interno",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
