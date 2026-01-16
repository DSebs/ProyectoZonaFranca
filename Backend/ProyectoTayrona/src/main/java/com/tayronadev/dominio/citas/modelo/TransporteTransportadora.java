package com.tayronadev.dominio.citas.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Implementación de transporte por transportadora
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class TransporteTransportadora extends OpcionTransporte {
    
    @NonNull
    private final String nombreTransportadora;
    @NonNull
    private final String numeroGuia;
    
    public TransporteTransportadora(@NonNull String nombreTransportadora, 
                                   @NonNull String numeroGuia, 
                                   DatosAuxiliar auxiliar) {
        super(auxiliar);
        this.nombreTransportadora = nombreTransportadora;
        this.numeroGuia = numeroGuia;
        validar();
    }
    
    public TransporteTransportadora(@NonNull String nombreTransportadora, @NonNull String numeroGuia) {
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
}