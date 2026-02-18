package com.tayronadev.dominio.auditoria.casosuso;

import com.tayronadev.dominio.auditoria.excepciones.RegistroNoEncontradoException;
import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import com.tayronadev.dominio.auditoria.repositorios.AuditoriaRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso para consultar registros de auditoría.
 * Proporciona diferentes métodos de consulta para los registros
 * de cambio de estado de las citas.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ConsultarAuditoriaUseCase {
    
    private final AuditoriaRepositorio auditoriaRepositorio;
    
    /**
     * Obtiene un registro de auditoría por su ID
     */
    public RegistroCambioEstado obtenerPorId(String registroId) {
        return auditoriaRepositorio.buscarPorId(registroId)
            .orElseThrow(() -> new RegistroNoEncontradoException(registroId));
    }
    
    /**
     * Obtiene todos los registros de auditoría para una cita específica
     */
    public List<RegistroCambioEstado> obtenerPorCitaId(String citaId) {
        log.debug("Consultando registros de auditoría para cita: {}", citaId);
        return auditoriaRepositorio.buscarPorCitaId(citaId);
    }
    
    /**
     * Obtiene todos los registros de auditoría realizados por un usuario.
     * Este es el filtro principal expuesto en la API.
     */
    public List<RegistroCambioEstado> obtenerPorUsuarioId(String usuarioId) {
        log.debug("Consultando registros de auditoría para usuario: {}", usuarioId);
        return auditoriaRepositorio.buscarPorUsuarioId(usuarioId);
    }
    
    /**
     * Obtiene registros filtrados por tipo de cambio
     */
    public List<RegistroCambioEstado> obtenerPorTipoCambio(TipoCambio tipoCambio) {
        log.debug("Consultando registros de auditoría por tipo de cambio: {}", tipoCambio);
        return auditoriaRepositorio.buscarPorTipoCambio(tipoCambio);
    }
    
    /**
     * Obtiene registros de auditoría en un rango de fechas
     */
    public List<RegistroCambioEstado> obtenerPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        log.debug("Consultando registros de auditoría entre {} y {}", desde, hasta);
        return auditoriaRepositorio.buscarPorRangoFechas(desde, hasta);
    }
    
    /**
     * Obtiene los últimos N registros de auditoría
     */
    public List<RegistroCambioEstado> obtenerUltimosRegistros(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0");
        }
        log.debug("Consultando últimos {} registros de auditoría", limite);
        return auditoriaRepositorio.obtenerUltimosRegistros(limite);
    }
    
    /**
     * Cuenta el total de cambios realizados por un usuario
     */
    public long contarPorUsuarioId(String usuarioId) {
        return auditoriaRepositorio.contarPorUsuarioId(usuarioId);
    }
    
    /**
     * Cuenta el total de cambios realizados en una cita
     */
    public long contarPorCitaId(String citaId) {
        return auditoriaRepositorio.contarPorCitaId(citaId);
    }
}
