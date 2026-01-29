package com.tayronadev.api.citas.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la información del proveedor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformacionProveedorRequest {
    
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 200, message = "El nombre del proveedor no puede exceder 200 caracteres")
    private String nombreProveedor;
    
    @NotBlank(message = "El NIT es obligatorio")
    @Size(max = 50, message = "El NIT no puede exceder 50 caracteres")
    private String nit;
    
    @NotBlank(message = "El número de orden de compra es obligatorio")
    @Size(max = 100, message = "El número de orden de compra no puede exceder 100 caracteres")
    private String numeroOrdenCompra;
    
    @NotNull(message = "Los datos del responsable son obligatorios")
    @Valid
    private DatosContactoRequest responsable;
}
