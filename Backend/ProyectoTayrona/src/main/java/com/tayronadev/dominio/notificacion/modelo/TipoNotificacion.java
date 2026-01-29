package com.tayronadev.dominio.notificacion.modelo;

/**
 * Tipos de notificación disponibles para el sistema de citas
 */
public enum TipoNotificacion {
    
    CITA_CONFIRMADA("Cita Confirmada", "Su cita ha sido confirmada exitosamente"),
    CITA_RECHAZADA("Cita Rechazada", "Su solicitud de cita ha sido rechazada"),
    CITA_CANCELADA("Cita Cancelada", "Su cita ha sido cancelada");
    
    private final String asunto;
    private final String descripcion;
    
    TipoNotificacion(String asunto, String descripcion) {
        this.asunto = asunto;
        this.descripcion = descripcion;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
