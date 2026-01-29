package com.tayronadev.api.citas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para consultar la disponibilidad de horarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarDisponibilidadRequest {
    
    @NotNull(message = "El tipo de cita es obligatorio")
    private TipoCita tipoCita;
    
    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
}
