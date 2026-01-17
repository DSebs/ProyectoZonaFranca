package com.tayronadev.api.citas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para cancelar una cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelarCitaRequest {
    
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    private String motivoCancelacion;
}
