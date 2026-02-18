package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.auditoria.casosuso.RegistrarCambioEstadoUseCase;
import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.EstadoPostCita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.GestorEstadosCita;
import com.tayronadev.dominio.notificacion.casosuso.NotificarCambioEstadoCitaUseCase;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.servicios.UsuarioActualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para gestionar los estados de las citas.
 * Integra el envío de notificaciones al proveedor cuando cambia el estado
 * y registra los cambios en el sistema de auditoría.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GestionarEstadoCitaUseCase {
    
    private final CitaRepositorio citaRepositorio;
    private final GestorEstadosCita gestorEstados;
    private final NotificarCambioEstadoCitaUseCase notificarCambioEstado;
    private final RegistrarCambioEstadoUseCase registrarCambioEstado;
    private final UsuarioActualService usuarioActualService;
    
    /**
     * Confirma una cita pendiente y notifica al proveedor
     */
    public Cita confirmarCita(String citaId, String observaciones) {
        var cita = obtenerCitaPorId(citaId);
        EstadoCita estadoAnterior = cita.getEstado();
        
        cita.confirmar(observaciones);
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioEnAuditoria(citaGuardada, estadoAnterior, observaciones);
        
        // Notificar al proveedor (asíncrono)
        notificarCambioEstado.ejecutar(citaGuardada, observaciones);
        
        log.info("Cita {} confirmada y notificación enviada", citaId);
        return citaGuardada;
    }
    
    /**
     * Rechaza una cita pendiente y notifica al proveedor con el motivo
     */
    public Cita rechazarCita(String citaId, String motivoRechazo) {
        var cita = obtenerCitaPorId(citaId);
        EstadoCita estadoAnterior = cita.getEstado();
        
        cita.rechazar(motivoRechazo);
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioEnAuditoria(citaGuardada, estadoAnterior, motivoRechazo);
        
        // Notificar al proveedor (asíncrono)
        notificarCambioEstado.ejecutar(citaGuardada, motivoRechazo);
        
        log.info("Cita {} rechazada y notificación enviada", citaId);
        return citaGuardada;
    }
    
    /**
     * Cancela una cita (pendiente o confirmada) y notifica al proveedor
     */
    public Cita cancelarCita(String citaId, String motivoCancelacion) {
        var cita = obtenerCitaPorId(citaId);
        EstadoCita estadoAnterior = cita.getEstado();
        
        cita.cancelar(motivoCancelacion);
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioEnAuditoria(citaGuardada, estadoAnterior, motivoCancelacion);
        
        // Notificar al proveedor (asíncrono)
        notificarCambioEstado.ejecutar(citaGuardada, motivoCancelacion);
        
        log.info("Cita {} cancelada y notificación enviada", citaId);
        return citaGuardada;
    }
    
    /**
     * Agrega observaciones a una cita
     */
    public Cita agregarObservaciones(String citaId, String observaciones) {
        var cita = obtenerCitaPorId(citaId);
        cita.agregarObservaciones(observaciones);
        return citaRepositorio.guardar(cita);
    }
    
    /**
     * Marca una cita como entregada
     */
    public Cita marcarComoEntregada(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        cita.marcarComoEntregada();
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioPostCitaEnAuditoria(citaGuardada, EstadoPostCita.ENTREGADO);
        
        return citaGuardada;
    }
    
    /**
     * Marca una cita como devuelta
     */
    public Cita marcarComoDevuelta(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        cita.marcarComoDevuelta();
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioPostCitaEnAuditoria(citaGuardada, EstadoPostCita.DEVUELTO);
        
        return citaGuardada;
    }
    
    /**
     * Marca una cita como tardía
     */
    public Cita marcarComoTardia(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        cita.marcarComoTardia();
        var citaGuardada = citaRepositorio.guardar(cita);
        
        // Registrar en auditoría
        registrarCambioPostCitaEnAuditoria(citaGuardada, EstadoPostCita.TARDIA);
        
        return citaGuardada;
    }
    
    /**
     * Verifica si una cita puede ser modificada por un administrador
     */
    @Transactional(readOnly = true)
    public boolean puedeSerModificadaPorAdministrador(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        return gestorEstados.puedeSerModificadaPorAdministrador(cita);
    }
    
    /**
     * Verifica si una cita puede ser cancelada por el proveedor
     */
    @Transactional(readOnly = true)
    public boolean puedeSerCanceladaPorProveedor(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        return gestorEstados.puedeSerCanceladaPorProveedor(cita);
    }
    
    private Cita obtenerCitaPorId(String citaId) {
        return citaRepositorio.buscarPorId(citaId)
                .orElseThrow(() -> new CitaNoEncontradaException(citaId));
    }
    
    /**
     * Registra un cambio de estado en el sistema de auditoría.
     * Solo se registra si hay un usuario autenticado.
     */
    private void registrarCambioEnAuditoria(Cita cita, EstadoCita estadoAnterior, String observaciones) {
        usuarioActualService.obtenerUsuarioActual().ifPresent(usuario -> {
            try {
                registrarCambioEstado.registrarCambioEstado(cita, usuario, estadoAnterior, observaciones);
                log.debug("Cambio de estado registrado en auditoría para cita {}", cita.getId());
            } catch (Exception e) {
                log.error("Error al registrar auditoría para cita {}: {}", cita.getId(), e.getMessage());
            }
        });
    }
    
    /**
     * Registra una asignación de estado post-cita en el sistema de auditoría.
     * Solo se registra si hay un usuario autenticado.
     */
    private void registrarCambioPostCitaEnAuditoria(Cita cita, EstadoPostCita estadoPostCita) {
        usuarioActualService.obtenerUsuarioActual().ifPresent(usuario -> {
            try {
                registrarCambioEstado.registrarAsignacionEstadoPostCita(cita, usuario, estadoPostCita);
                log.debug("Asignación de estado post-cita registrada en auditoría para cita {}", cita.getId());
            } catch (Exception e) {
                log.error("Error al registrar auditoría post-cita para cita {}: {}", cita.getId(), e.getMessage());
            }
        });
    }
}
