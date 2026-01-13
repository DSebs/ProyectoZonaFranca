package com.tayronadev.dominio.citas.modelo;

/**
 * Estados posibles de una cita en el sistema
 */
public enum EstadoCita {
    PENDIENTE("Pendiente de aprobación"),
    CONFIRMADA("Confirmada"),
    RECHAZADA("Rechazada"),
    CANCELADA("Cancelada");
    
    private final String descripcion;
    
    EstadoCita(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el estado permite modificaciones
     */
    public boolean permiteModificacion() {
        return this == PENDIENTE || this == CONFIRMADA;
    }
    
    /**
     * Verifica si el estado es final (no permite cambios posteriores)
     */
    public boolean esFinal() {
        return this == RECHAZADA || this == CANCELADA;
    }
}
