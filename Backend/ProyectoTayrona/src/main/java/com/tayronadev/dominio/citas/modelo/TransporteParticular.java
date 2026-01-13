package com.tayronadev.dominio.citas.modelo;

import java.util.Objects;

/**
 * Implementación de transporte particular
 */
public class TransporteParticular extends OpcionTransporte {
    
    private final DatosConductor conductor;
    
    public TransporteParticular(DatosConductor conductor, DatosAuxiliar auxiliar) {
        super(auxiliar);
        this.conductor = Objects.requireNonNull(conductor, "Los datos del conductor son obligatorios");
        validar();
    }
    
    public TransporteParticular(DatosConductor conductor) {
        this(conductor, null);
    }
    
    @Override
    public TipoTransporte getTipo() {
        return TipoTransporte.PARTICULAR;
    }
    
    @Override
    public void validar() {
        // La validación específica se hace en el record DatosConductor
    }
    
    public DatosConductor getConductor() {
        return conductor;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransporteParticular that = (TransporteParticular) o;
        return Objects.equals(conductor, that.conductor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(conductor);
    }
    
    /**
     * Record que encapsula los datos del conductor
     */
    public record DatosConductor(String nombre, String cedula, String placaVehiculo) {
        public DatosConductor {
            Objects.requireNonNull(nombre, "El nombre del conductor es obligatorio");
            Objects.requireNonNull(cedula, "La cédula del conductor es obligatoria");
            Objects.requireNonNull(placaVehiculo, "La placa del vehículo es obligatoria");
            
            if (nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del conductor no puede estar vacío");
            }
            if (cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula del conductor no puede estar vacía");
            }
            if (placaVehiculo.trim().isEmpty()) {
                throw new IllegalArgumentException("La placa del vehículo no puede estar vacía");
            }
        }
    }
}