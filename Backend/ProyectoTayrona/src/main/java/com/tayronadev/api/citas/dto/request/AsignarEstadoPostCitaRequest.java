package com.tayronadev.api.citas.dto.request;

import com.tayronadev.dominio.citas.modelo.EstadoPostCita;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para asignar un estado post-cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarEstadoPostCitaRequest {
    
    @NotNull(message = "El estado post-cita es obligatorio")
    private EstadoPostCita estadoPostCita;
}
