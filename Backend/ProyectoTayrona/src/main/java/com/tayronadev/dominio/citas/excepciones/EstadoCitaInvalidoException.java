package com.tayronadev.dominio.citas.excepciones;

import com.tayronadev.dominio.citas.excepciones.CitaException;
import com.tayronadev.dominio.citas.modelo.EstadoCita;

/**
 * Excepción lanzada cuando se intenta realizar una operación no válida según el estado actual de la cita
 */
public class EstadoCitaInvalidoException extends CitaException {
    
    private final EstadoCita estadoActual;
    private final String operacionIntentada;
    
    public EstadoCitaInvalidoException(EstadoCita estadoActual, String operacionIntentada) {
        super(String.format("No se puede realizar la operación '%s' cuando la cita está en estado %s", 
              operacionIntentada, estadoActual.getDescripcion()));
        this.estadoActual = estadoActual;
        this.operacionIntentada = operacionIntentada;
    }
    
    public EstadoCita getEstadoActual() {
        return estadoActual;
    }
    
    public String getOperacionIntentada() {
        return operacionIntentada;
    }
}