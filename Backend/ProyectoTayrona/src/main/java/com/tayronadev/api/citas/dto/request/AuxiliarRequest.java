package com.tayronadev.api.citas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para los datos del auxiliar de transporte (opcional)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuxiliarRequest {
    
    @NotBlank(message = "El nombre del auxiliar es obligatorio")
    @Size(max = 100, message = "El nombre del auxiliar no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "La cédula del auxiliar es obligatoria")
    @Size(max = 20, message = "La cédula del auxiliar no puede exceder 20 caracteres")
    private String cedula;
}
