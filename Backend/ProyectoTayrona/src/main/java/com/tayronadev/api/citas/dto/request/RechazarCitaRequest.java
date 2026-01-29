package com.tayronadev.api.citas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para rechazar una cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechazarCitaRequest {
    
    @NotBlank(message = "El motivo de rechazo es obligatorio")
    @Size(max = 500, message = "El motivo de rechazo no puede exceder 500 caracteres")
    private String motivoRechazo;
}
