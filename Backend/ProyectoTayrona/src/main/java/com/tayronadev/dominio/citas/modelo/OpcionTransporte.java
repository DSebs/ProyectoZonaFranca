package com.tayronadev.dominio.citas.modelo;

import java.util.Objects;
import java.util.Optional;

/**
 * Clase abstracta que representa las opciones de transporte disponibles
 */
public abstract class OpcionTransporte {
    
    private final DatosAuxiliar auxiliar;
    
    protected OpcionTransporte(DatosAuxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }
    
    public Optional<DatosAuxiliar> getAuxiliar() {
        return Optional.ofNullable(auxiliar);
    }
    
    /**
     * Método abstracto para obtener el tipo de transporte
     */
    public abstract TipoTransporte getTipo();
    
    /**
     * Método abstracto para validar los datos específicos del transporte
     */
    public abstract void validar();
    
    /**
     * Enum para los tipos de transporte
     */
    public enum TipoTransporte {
        TRANSPORTADORA("Transportadora"),
        PARTICULAR("Particular");
        
        private final String descripcion;
        
        TipoTransporte(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    /**
     * Record para los datos del auxiliar de transporte (opcional)
     */
    public record DatosAuxiliar(String nombre, String cedula) {
        public DatosAuxiliar {
            Objects.requireNonNull(nombre, "El nombre del auxiliar es obligatorio");
            Objects.requireNonNull(cedula, "La cédula del auxiliar es obligatoria");
            
            if (nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del auxiliar no puede estar vacío");
            }
            if (cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula del auxiliar no puede estar vacía");
            }
        }
    }
}