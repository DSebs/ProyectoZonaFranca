package com.tayronadev.dominio.citas.repositorios;

import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interface) del repositorio de citas.
 * Define las operaciones de persistencia necesarias para el dominio.
 */
public interface CitaRepositorio {
    
    /**
     * Guarda una nueva cita o actualiza una existente
     */
    Cita guardar(Cita cita);
    
    /**
     * Busca una cita por su ID
     */
    Optional<Cita> buscarPorId(String id);
    
    /**
     * Busca todas las citas por estado
     */
    List<Cita> buscarPorEstado(EstadoCita estado);
    
    /**
     * Busca citas por tipo y estado
     */
    List<Cita> buscarPorTipoYEstado(TipoCita tipo, EstadoCita estado);
    
    /**
     * Busca citas por NIT del proveedor
     */
    List<Cita> buscarPorNitProveedor(String nit);
    
    /**
     * Busca citas en un rango de fechas
     */
    List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca citas por fecha específica
     */
    List<Cita> buscarPorFecha(LocalDate fecha);
    
    /**
     * Busca citas por tipo en una fecha específica
     */
    List<Cita> buscarPorTipoYFecha(TipoCita tipo, LocalDate fecha);
    
    /**
     * Busca citas activas (PENDIENTE o CONFIRMADA) por tipo
     */
    List<Cita> buscarActivasPorTipo(TipoCita tipo);
    
    /**
     * Busca citas que tienen conflicto de horario
     */
    List<Cita> buscarConflictoHorario(TipoCita tipo, LocalDateTime fechaHora);
    
    /**
     * Cuenta el número de citas por estado
     */
    long contarPorEstado(EstadoCita estado);
    
    /**
     * Cuenta el número de citas por tipo y estado
     */
    long contarPorTipoYEstado(TipoCita tipo, EstadoCita estado);
    
    /**
     * Verifica si existe una cita con el ID especificado
     */
    boolean existePorId(String id);
    
    /**
     * Elimina una cita (solo para casos excepcionales)
     */
    void eliminar(String id);
    
    /**
     * Obtiene todas las citas (con paginación en implementación)
     */
    List<Cita> obtenerTodas();
}