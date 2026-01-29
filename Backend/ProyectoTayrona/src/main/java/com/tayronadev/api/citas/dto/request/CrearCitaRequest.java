package com.tayronadev.api.citas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para crear una nueva cita.
 * Contiene toda la información necesaria del formulario del proveedor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearCitaRequest {
    
    @NotNull(message = "El tipo de cita es obligatorio")
    private TipoCita tipoCita;
    
    @NotNull(message = "La información del proveedor es obligatoria")
    @Valid
    private InformacionProveedorRequest proveedor;
    
    @NotNull(message = "La información de transporte es obligatoria")
    @Valid
    private TransporteRequest transporte;
    
    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;
}
