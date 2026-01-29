package com.tayronadev.dominio.notificacion.excepciones;

/**
 * Excepción base para errores relacionados con el envío de notificaciones
 */
public class NotificacionException extends RuntimeException {
    
    public NotificacionException(String mensaje) {
        super(mensaje);
    }
    
    public NotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
