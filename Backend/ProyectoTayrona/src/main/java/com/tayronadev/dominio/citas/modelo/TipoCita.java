package com.tayronadev.dominio.citas.modelo;

/**
 * Tipos de cita disponibles en el sistema de agendamiento
 */
public enum TipoCita {
    ENTREGA("Entrega de producto"),
    RECOJO("Recojo de producto"), 
    IMPORTACION("Importación"),
    DEVOLUCION("Devolución");
    
    private final String descripcion;
    
    TipoCita(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
