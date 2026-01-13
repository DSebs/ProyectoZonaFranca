package com.tayronadev.dominio.citas.modelo;

import java.util.Objects;

/**
 * Value Object que encapsula toda la información del proveedor
 */
public record InformacionProveedor(
    String nombreProveedor,
    String nit,
    String numeroOrdenCompra,
    DatosContacto responsable
) {
    
    public InformacionProveedor {
        Objects.requireNonNull(nombreProveedor, "El nombre del proveedor es obligatorio");
        Objects.requireNonNull(nit, "El NIT es obligatorio");
        Objects.requireNonNull(numeroOrdenCompra, "El número de orden de compra es obligatorio");
        Objects.requireNonNull(responsable, "Los datos del responsable son obligatorios");
        
        if (nombreProveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío");
        }
        if (nit.trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT no puede estar vacío");
        }
        if (numeroOrdenCompra.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de orden de compra no puede estar vacío");
        }
    }
}