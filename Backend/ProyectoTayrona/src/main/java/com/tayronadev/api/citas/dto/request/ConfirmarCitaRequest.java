package com.tayronadev.api.citas.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para confirmar una cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmarCitaRequest {
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
