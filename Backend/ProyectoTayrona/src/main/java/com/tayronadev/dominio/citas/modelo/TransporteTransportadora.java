package com.tayronadev.dominio.citas.modelo;

import java.util.Objects;

/**
 * Implementación de transporte por transportadora
 */
public class TransporteTransportadora extends OpcionTransporte {
    
    private final String nombreTransportadora;
    private final String numeroGuia;
    
    public TransporteTransportadora(String nombreTransportadora, String numeroGuia, DatosAuxiliar auxiliar) {
        super(auxiliar);
        this.nombreTransportadora = Objects.requireNonNull(nombreTransportadora, "El nombre de la transportadora es obligatorio");
        this.numeroGuia = Objects.requireNonNull(numeroGuia, "El número de guía es obligatorio");
        validar();
    }
    
    public TransporteTransportadora(String nombreTransportadora, String numeroGuia) {
        this(nombreTransportadora, numeroGuia, null);
    }
    
    @Override
    public TipoTransporte getTipo() {
        return TipoTransporte.TRANSPORTADORA;
    }
    
    @Override
    public void validar() {
        if (nombreTransportadora.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la transportadora no puede estar vacío");
        }
        if (numeroGuia.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de guía no puede estar vacío");
        }
    }
    
    public String getNombreTransportadora() {
        return nombreTransportadora;
    }
    
    public String getNumeroGuia() {
        return numeroGuia;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransporteTransportadora that = (TransporteTransportadora) o;
        return Objects.equals(nombreTransportadora, that.nombreTransportadora) &&
               Objects.equals(numeroGuia, that.numeroGuia);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombreTransportadora, numeroGuia);
    }
}