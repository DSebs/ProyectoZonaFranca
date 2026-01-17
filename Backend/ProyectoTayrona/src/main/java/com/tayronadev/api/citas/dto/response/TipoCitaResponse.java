package com.tayronadev.api.citas.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * DTO de respuesta para los tipos de cita disponibles
 */
@Value
@Builder
public class TipoCitaResponse {
    String codigo;
    String descripcion;
    List<Integer> horariosPermitidos;
}
