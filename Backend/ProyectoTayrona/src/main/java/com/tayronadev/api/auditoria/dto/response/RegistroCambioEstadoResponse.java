package com.tayronadev.api.auditoria.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un registro de cambio de estado.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroCambioEstadoResponse {
    
    private String id;
    private String citaId;
    private String usuarioId;
    private String usuarioNombre;
    private TipoCambio tipoCambio;
    private String tipoCambioDescripcion;
    private String estadoAnterior;
    private String estadoNuevo;
    private String observaciones;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCambio;
    
    private boolean esCambioEstadoPrincipal;
    private boolean esCambioEstadoPostCita;
}
