package com.tayronadev.api.auditoria.mappers;

import com.tayronadev.api.auditoria.dto.response.RegistroCambioEstadoResponse;
import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre modelos de dominio de auditoría y DTOs de la API.
 */
@Component
public class AuditoriaDtoMapper {
    
    /**
     * Convierte un modelo de dominio a DTO de respuesta
     */
    public RegistroCambioEstadoResponse toResponse(RegistroCambioEstado registro) {
        if (registro == null) {
            return null;
        }
        
        return RegistroCambioEstadoResponse.builder()
            .id(registro.getId())
            .citaId(registro.getCitaId())
            .usuarioId(registro.getUsuarioId())
            .usuarioNombre(registro.getUsuarioNombre())
            .tipoCambio(registro.getTipoCambio())
            .tipoCambioDescripcion(registro.getTipoCambio().getDescripcion())
            .estadoAnterior(registro.getEstadoAnterior())
            .estadoNuevo(registro.getEstadoNuevo())
            .observaciones(registro.getObservaciones())
            .fechaCambio(registro.getFechaCambio())
            .esCambioEstadoPrincipal(registro.esCambioEstadoPrincipal())
            .esCambioEstadoPostCita(registro.esCambioEstadoPostCita())
            .build();
    }
    
    /**
     * Convierte una lista de modelos de dominio a lista de DTOs
     */
    public List<RegistroCambioEstadoResponse> toResponseList(List<RegistroCambioEstado> registros) {
        return registros.stream()
            .map(this::toResponse)
            .toList();
    }
}
