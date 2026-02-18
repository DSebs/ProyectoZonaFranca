package com.tayronadev.dominio.auditoria.repositorios;

import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de auditoría.
 * Define las operaciones de persistencia para los registros de cambio de estado.
 */
public interface AuditoriaRepositorio {
    
    /**
     * Guarda un nuevo registro de auditoría
     */
    RegistroCambioEstado guardar(RegistroCambioEstado registro);
    
    /**
     * Busca un registro por su ID
     */
    Optional<RegistroCambioEstado> buscarPorId(String id);
    
    /**
     * Obtiene todos los registros de auditoría para una cita específica
     */
    List<RegistroCambioEstado> buscarPorCitaId(String citaId);
    
    /**
     * Obtiene todos los registros de auditoría realizados por un usuario
     */
    List<RegistroCambioEstado> buscarPorUsuarioId(String usuarioId);
    
    /**
     * Obtiene registros filtrados por tipo de cambio
     */
    List<RegistroCambioEstado> buscarPorTipoCambio(TipoCambio tipoCambio);
    
    /**
     * Obtiene registros de auditoría en un rango de fechas
     */
    List<RegistroCambioEstado> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta);
    
    /**
     * Obtiene los últimos N registros de auditoría
     */
    List<RegistroCambioEstado> obtenerUltimosRegistros(int limite);
    
    /**
     * Cuenta el total de cambios realizados por un usuario
     */
    long contarPorUsuarioId(String usuarioId);
    
    /**
     * Cuenta el total de cambios realizados en una cita
     */
    long contarPorCitaId(String citaId);
}
