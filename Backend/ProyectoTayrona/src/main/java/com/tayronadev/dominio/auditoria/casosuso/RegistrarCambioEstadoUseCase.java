package com.tayronadev.dominio.auditoria.casosuso;

import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import com.tayronadev.dominio.auditoria.repositorios.AuditoriaRepositorio;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.EstadoPostCita;
import com.tayronadev.dominio.usuario.modelo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para registrar cambios de estado en las citas.
 * Este caso de uso es invocado cada vez que un usuario cambia el estado
 * de una cita o asigna un estado post-cita.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegistrarCambioEstadoUseCase {
    
    private final AuditoriaRepositorio auditoriaRepositorio;
    
    /**
     * Registra un cambio de estado principal en una cita.
     * 
     * @param cita La cita que fue modificada
     * @param usuario El usuario que realizó el cambio
     * @param estadoAnterior El estado anterior de la cita
     * @param observaciones Observaciones opcionales del cambio
     * @return El registro de auditoría creado
     */
    public RegistroCambioEstado registrarCambioEstado(Cita cita, 
                                                       User usuario,
                                                       EstadoCita estadoAnterior,
                                                       String observaciones) {
        TipoCambio tipoCambio = determinarTipoCambio(cita.getEstado());
        
        RegistroCambioEstado registro = new RegistroCambioEstado(
            cita.getId(),
            usuario.getId(),
            usuario.getNombre(),
            tipoCambio,
            estadoAnterior != null ? estadoAnterior.name() : null,
            cita.getEstado().name(),
            observaciones
        );
        
        RegistroCambioEstado registroGuardado = auditoriaRepositorio.guardar(registro);
        
        log.info("Registrado cambio de estado: Usuario '{}' cambió cita '{}' de {} a {}",
            usuario.getNombre(), cita.getId(), estadoAnterior, cita.getEstado());
        
        return registroGuardado;
    }
    
    /**
     * Registra la asignación de un estado post-cita.
     * 
     * @param cita La cita a la que se le asignó el estado post-cita
     * @param usuario El usuario que realizó la asignación
     * @param estadoPostCita El estado post-cita asignado
     * @return El registro de auditoría creado
     */
    public RegistroCambioEstado registrarAsignacionEstadoPostCita(Cita cita,
                                                                   User usuario,
                                                                   EstadoPostCita estadoPostCita) {
        TipoCambio tipoCambio = determinarTipoCambioPostCita(estadoPostCita);
        
        RegistroCambioEstado registro = new RegistroCambioEstado(
            cita.getId(),
            usuario.getId(),
            usuario.getNombre(),
            tipoCambio,
            null, // No hay estado anterior para post-cita
            estadoPostCita.name(),
            null // Sin observaciones adicionales
        );
        
        RegistroCambioEstado registroGuardado = auditoriaRepositorio.guardar(registro);
        
        log.info("Registrada asignación de estado post-cita: Usuario '{}' asignó {} a cita '{}'",
            usuario.getNombre(), estadoPostCita, cita.getId());
        
        return registroGuardado;
    }
    
    /**
     * Determina el tipo de cambio basándose en el estado actual de la cita
     */
    private TipoCambio determinarTipoCambio(EstadoCita estado) {
        return switch (estado) {
            case CONFIRMADA -> TipoCambio.CONFIRMACION;
            case RECHAZADA -> TipoCambio.RECHAZO;
            case CANCELADA -> TipoCambio.CANCELACION;
            case PENDIENTE -> throw new IllegalArgumentException(
                "No se puede registrar cambio a estado PENDIENTE");
        };
    }
    
    /**
     * Determina el tipo de cambio para estados post-cita
     */
    private TipoCambio determinarTipoCambioPostCita(EstadoPostCita estado) {
        return switch (estado) {
            case ENTREGADO -> TipoCambio.ASIGNACION_ENTREGADO;
            case DEVUELTO -> TipoCambio.ASIGNACION_DEVUELTO;
            case TARDIA -> TipoCambio.ASIGNACION_TARDIA;
        };
    }
}
