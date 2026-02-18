package com.tayronadev.dominio.auditoria.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un registro de auditoría.
 */
public class RegistroNoEncontradoException extends AuditoriaException {
    
    private static final String MENSAJE = "No se encontró el registro de auditoría con ID: %s";
    
    public RegistroNoEncontradoException(String registroId) {
        super(String.format(MENSAJE, registroId));
    }
}
