package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.GestorEstadosCita;
import com.tayronadev.dominio.notificacion.casosuso.NotificarCambioEstadoCitaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para gestionar los estados de las citas.
 * Integra el envío de notificaciones al proveedor cuando cambia el estado.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GestionarEstadoCitaUseCase {
    
    private final CitaRepositorio citaRepositorio;
    private final GestorEstadosCita gestorEstados;
    private final NotificarCambioEstadoCitaUseCase notificarCambioEstado;
    
    /**
     * Confirma una cita pendiente y notifica al proveedor
     */
    public Cita confirmarCita(String citaId, String observaciones) {
        var cita = obtenerCitaPorId(citaId);
        cita.confirmar(observaciones);
        var citaGuardada = citaRepositorio.guardar(cita);
        
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
        cita.rechazar(motivoRechazo);
        var citaGuardada = citaRepositorio.guardar(cita);
        
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
        cita.cancelar(motivoCancelacion);
        var citaGuardada = citaRepositorio.guardar(cita);
        
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
        return citaRepositorio.guardar(cita);
    }
    
    /**
     * Marca una cita como devuelta
     */
    public Cita marcarComoDevuelta(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        cita.marcarComoDevuelta();
        return citaRepositorio.guardar(cita);
    }
    
    /**
     * Marca una cita como tardía
     */
    public Cita marcarComoTardia(String citaId) {
        var cita = obtenerCitaPorId(citaId);
        cita.marcarComoTardia();
        return citaRepositorio.guardar(cita);
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
}
