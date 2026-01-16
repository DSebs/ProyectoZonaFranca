package com.tayronadev.infraestructura.persistencia.mappers;

import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.infraestructura.persistencia.entidades.CitaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio y entidades JPA
 */
@Component
public class CitaMapper {
    
    /**
     * Convierte una entidad de dominio a entidad JPA
     */
    public CitaEntity toEntity(Cita cita) {
        if (cita == null) return null;
        
        var entity = new CitaEntity();
        
        // Datos básicos
        entity.setId(cita.getId());
        entity.setTipoCita(cita.getTipoCita());
        entity.setEstado(cita.getEstado());
        entity.setEstadoPostCita(cita.getEstadoPostCita().orElse(null));
        
        // Información del proveedor
        var proveedor = cita.getProveedor();
        entity.setNombreProveedor(proveedor.getNombreProveedor());
        entity.setNit(proveedor.getNit());
        entity.setNumeroOrdenCompra(proveedor.getNumeroOrdenCompra());
        
        // Datos del responsable
        var responsable = proveedor.getResponsable();
        entity.setResponsableNombre(responsable.getNombre());
        entity.setResponsableEmail(responsable.getEmail());
        entity.setResponsableTelefono(responsable.getTelefono());
        
        // Información de transporte
        var transporte = cita.getTransporte();
        mapearTransporte(transporte, entity);
        
        // Auxiliar de transporte (opcional)
        transporte.getAuxiliar().ifPresent(auxiliar -> {
            entity.setAuxiliarNombre(auxiliar.getNombre());
            entity.setAuxiliarCedula(auxiliar.getCedula());
        });
        
        // Horario
        entity.setFechaHora(cita.getHorario().getFechaHora());
        
        // Observaciones
        entity.setObservaciones(cita.getObservaciones().orElse(null));
        
        // Auditoría
        entity.setFechaCreacion(cita.getFechaCreacion());
        entity.setFechaUltimaModificacion(cita.getFechaUltimaModificacion());
        
        return entity;
    }
    
    /**
     * Convierte una entidad JPA a entidad de dominio
     */
    public Cita toDomain(CitaEntity entity) {
        if (entity == null) return null;
        
        // Crear datos de contacto del responsable
        var responsable = new DatosContacto(
            entity.getResponsableNombre(),
            entity.getResponsableEmail(),
            entity.getResponsableTelefono()
        );
        
        // Crear información del proveedor
        var proveedor = new InformacionProveedor(
            entity.getNombreProveedor(),
            entity.getNit(),
            entity.getNumeroOrdenCompra(),
            responsable
        );
        
        // Crear opción de transporte
        var transporte = crearOpcionTransporte(entity);
        
        // Crear horario
        var horario = new Horario(entity.getFechaHora());
        
        // Crear cita usando constructor de reconstrucción
        return new Cita(
            entity.getId(),
            entity.getTipoCita(),
            proveedor,
            transporte,
            horario,
            entity.getEstado(),
            entity.getObservaciones(),
            entity.getFechaCreacion(),
            entity.getFechaUltimaModificacion()
        );
    }
    
    private void mapearTransporte(OpcionTransporte transporte, CitaEntity entity) {
        if (transporte instanceof TransporteTransportadora transportadora) {
            entity.setTipoTransporte(CitaEntity.TipoTransporteEntity.TRANSPORTADORA);
            entity.setNombreTransportadora(transportadora.getNombreTransportadora());
            entity.setNumeroGuia(transportadora.getNumeroGuia());
        } else if (transporte instanceof TransporteParticular particular) {
            entity.setTipoTransporte(CitaEntity.TipoTransporteEntity.PARTICULAR);
            var conductor = particular.getConductor();
            entity.setConductorNombre(conductor.getNombre());
            entity.setConductorCedula(conductor.getCedula());
            entity.setPlacaVehiculo(conductor.getPlacaVehiculo());
        }
    }
    
    private OpcionTransporte crearOpcionTransporte(CitaEntity entity) {
        // Crear auxiliar si existe
        OpcionTransporte.DatosAuxiliar auxiliar = null;
        if (entity.getAuxiliarNombre() != null && entity.getAuxiliarCedula() != null) {
            auxiliar = new OpcionTransporte.DatosAuxiliar(
                entity.getAuxiliarNombre(),
                entity.getAuxiliarCedula()
            );
        }
        
        return switch (entity.getTipoTransporte()) {
            case TRANSPORTADORA -> new TransporteTransportadora(
                entity.getNombreTransportadora(),
                entity.getNumeroGuia(),
                auxiliar
            );
            case PARTICULAR -> {
                var conductor = new TransporteParticular.DatosConductor(
                    entity.getConductorNombre(),
                    entity.getConductorCedula(),
                    entity.getPlacaVehiculo()
                );
                yield new TransporteParticular(conductor, auxiliar);
            }
        };
    }
}