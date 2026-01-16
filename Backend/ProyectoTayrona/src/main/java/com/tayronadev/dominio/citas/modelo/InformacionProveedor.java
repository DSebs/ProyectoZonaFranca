package com.tayronadev.dominio.citas.modelo;

import lombok.NonNull;
import lombok.Value;

/**
 * Value Object que encapsula toda la información del proveedor
 */
@Value
public class InformacionProveedor {
    @NonNull String nombreProveedor;
    @NonNull String nit;
    @NonNull String numeroOrdenCompra;
    @NonNull DatosContacto responsable;
    
    public InformacionProveedor(@NonNull String nombreProveedor, 
                               @NonNull String nit, 
                               @NonNull String numeroOrdenCompra, 
                               @NonNull DatosContacto responsable) {
        if (nombreProveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío");
        }
        if (nit.trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT no puede estar vacío");
        }
        if (numeroOrdenCompra.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de orden de compra no puede estar vacío");
        }
        
        this.nombreProveedor = nombreProveedor;
        this.nit = nit;
        this.numeroOrdenCompra = numeroOrdenCompra;
        this.responsable = responsable;
    }
}