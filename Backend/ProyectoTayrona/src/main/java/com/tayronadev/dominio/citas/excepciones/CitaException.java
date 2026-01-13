package com.tayronadev.dominio.citas.modelo.excepciones;

/**
 * Excepción base para todas las excepciones relacionadas con el dominio de citas
 */
public abstract class CitaException extends RuntimeException {
    
    protected CitaException(String mensaje) {
        super(mensaje);
    }
    
    protected CitaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}