package com.tayronadev.dominio.auditoria.modelo;

/**
 * Enum que representa los tipos de cambio que pueden ser auditados en una cita.
 * Incluye cambios de estado principal y asignación de estados post-cita.
 */
public enum TipoCambio {
    
    // Cambios de estado principal
    CONFIRMACION("La cita fue confirmada"),
    RECHAZO("La cita fue rechazada"),
    CANCELACION("La cita fue cancelada"),
    
    // Cambios de estado post-cita
    ASIGNACION_ENTREGADO("Se asignó estado post-cita: ENTREGADO"),
    ASIGNACION_DEVUELTO("Se asignó estado post-cita: DEVUELTO"),
    ASIGNACION_TARDIA("Se asignó estado post-cita: TARDIA");
    
    private final String descripcion;
    
    TipoCambio(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
