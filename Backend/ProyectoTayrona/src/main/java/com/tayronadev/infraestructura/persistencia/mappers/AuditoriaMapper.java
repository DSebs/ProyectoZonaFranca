package com.tayronadev.infraestructura.persistencia.mappers;

import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import com.tayronadev.infraestructura.persistencia.entidades.RegistroAuditoriaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre el modelo de dominio RegistroCambioEstado
 * y la entidad JPA RegistroAuditoriaEntity.
 */
@Component
public class AuditoriaMapper {
    
    /**
     * Convierte un modelo de dominio a entidad JPA
     */
    public RegistroAuditoriaEntity toEntity(RegistroCambioEstado registro) {
        if (registro == null) {
            return null;
        }
        
        RegistroAuditoriaEntity entity = new RegistroAuditoriaEntity();
        entity.setId(registro.getId());
        entity.setCitaId(registro.getCitaId());
        entity.setUsuarioId(registro.getUsuarioId());
        entity.setUsuarioNombre(registro.getUsuarioNombre());
        entity.setTipoCambio(registro.getTipoCambio());
        entity.setEstadoAnterior(registro.getEstadoAnterior());
        entity.setEstadoNuevo(registro.getEstadoNuevo());
        entity.setObservaciones(registro.getObservaciones());
        entity.setFechaCambio(registro.getFechaCambio());
        
        return entity;
    }
    
    /**
     * Convierte una entidad JPA a modelo de dominio
     */
    public RegistroCambioEstado toDomain(RegistroAuditoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new RegistroCambioEstado(
            entity.getId(),
            entity.getCitaId(),
            entity.getUsuarioId(),
            entity.getUsuarioNombre(),
            entity.getTipoCambio(),
            entity.getEstadoAnterior(),
            entity.getEstadoNuevo(),
            entity.getObservaciones(),
            entity.getFechaCambio()
        );
    }
}
