package com.tayronadev.dominio.citas.modelo;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Optional;

/**
 * Clase abstracta que representa las opciones de transporte disponibles
 */
@Getter
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
        
        @Getter
        private final String descripcion;
        
        TipoTransporte(String descripcion) {
            this.descripcion = descripcion;
        }
    }
    
    /**
     * Value Object para los datos del auxiliar de transporte (opcional)
     */
    @Value
    public static class DatosAuxiliar {
        @NonNull String nombre;
        @NonNull String cedula;
        
        public DatosAuxiliar(@NonNull String nombre, @NonNull String cedula) {
            if (nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del auxiliar no puede estar vacío");
            }
            if (cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula del auxiliar no puede estar vacía");
            }
            
            this.nombre = nombre;
            this.cedula = cedula;
        }
    }
}