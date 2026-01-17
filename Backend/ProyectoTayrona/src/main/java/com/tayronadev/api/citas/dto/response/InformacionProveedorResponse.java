package com.tayronadev.api.citas.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * DTO de respuesta para la información del proveedor
 */
@Value
@Builder
public class InformacionProveedorResponse {
    String nombreProveedor;
    String nit;
    String numeroOrdenCompra;
    DatosContactoResponse responsable;
}
