package com.tayronadev.dominio.citas.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

/**
 * Implementación de transporte particular
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class TransporteParticular extends OpcionTransporte {
    
    @NonNull
    private final DatosConductor conductor;
    
    public TransporteParticular(@NonNull DatosConductor conductor, DatosAuxiliar auxiliar) {
        super(auxiliar);
        this.conductor = conductor;
        validar();
    }
    
    public TransporteParticular(@NonNull DatosConductor conductor) {
        this(conductor, null);
    }
    
    @Override
    public TipoTransporte getTipo() {
        return TipoTransporte.PARTICULAR;
    }
    
    @Override
    public void validar() {
        // La validación específica se hace en el Value Object DatosConductor
    }
    
    /**
     * Value Object que encapsula los datos del conductor
     */
    @Value
    public static class DatosConductor {
        @NonNull String nombre;
        @NonNull String cedula;
        @NonNull String placaVehiculo;
        
        public DatosConductor(@NonNull String nombre, @NonNull String cedula, @NonNull String placaVehiculo) {
            if (nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del conductor no puede estar vacío");
            }
            if (cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula del conductor no puede estar vacía");
            }
            if (placaVehiculo.trim().isEmpty()) {
                throw new IllegalArgumentException("La placa del vehículo no puede estar vacía");
            }
            
            this.nombre = nombre;
            this.cedula = cedula;
            this.placaVehiculo = placaVehiculo;
        }
    }
}