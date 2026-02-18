package com.tayronadev.dominio.auditoria.excepciones;

/**
 * Excepción base para el dominio de auditoría.
 */
public class AuditoriaException extends RuntimeException {
    
    public AuditoriaException(String mensaje) {
        super(mensaje);
    }
    
    public AuditoriaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
