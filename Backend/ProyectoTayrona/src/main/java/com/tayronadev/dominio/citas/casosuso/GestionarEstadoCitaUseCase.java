package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.GestorEstadosCita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para gestionar los estados de las citas
 */
@Service
@Transactional
@RequiredArgsConstructor
public class GestionarEstadoCitaUseCase {
    
    private final CitaRepositorio citaRepositorio;
    private final GestorEstadosCita gestorEstados;
    
    /**
     * Confirma una cita pendiente
     */
    public Cita confirmarCita(String citaId, String observaciones) {
        var cita = obtenerCitaPorId(citaId);
        cita.confirmar(observaciones);
        return citaRepositorio.guardar(cita);
    }
    
    /**
     * Rechaza una cita pendiente
     */
    public Cita rechazarCita(String citaId, String motivoRechazo) {
        var cita = obtenerCitaPorId(citaId);
        cita.rechazar(motivoRechazo);
        return citaRepositorio.guardar(cita);
    }
    
    /**
     * Cancela una cita (pendiente o confirmada)
     */
    public Cita cancelarCita(String citaId, String motivoCancelacion) {
        var cita = obtenerCitaPorId(citaId);
        cita.cancelar(motivoCancelacion);
        return citaRepositorio.guardar(cita);
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