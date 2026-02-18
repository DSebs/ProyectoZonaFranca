package com.tayronadev.api.auditoria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta con resumen de auditoría.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenAuditoriaResponse {
    
    private String usuarioId;
    private String usuarioNombre;
    private long totalCambiosRealizados;
    private List<RegistroCambioEstadoResponse> ultimosCambios;
}
