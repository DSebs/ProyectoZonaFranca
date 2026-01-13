package com.tayronadev.dominio.citas.modelo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra una cita específica
 */
public class CitaNoEncontradaException extends CitaException {
    
    private final String identificadorCita;
    
    public CitaNoEncontradaException(String identificadorCita) {
        super(String.format("No se encontró la cita con identificador: %s", identificadorCita));
        this.identificadorCita = identificadorCita;
    }
    
    public String getIdentificadorCita() {
        return identificadorCita;
    }
}