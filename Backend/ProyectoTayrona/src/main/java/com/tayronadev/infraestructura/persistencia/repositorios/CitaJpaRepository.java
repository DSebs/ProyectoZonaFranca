package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import com.tayronadev.infraestructura.persistencia.entidades.CitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para entidades de cita
 */
@Repository
public interface CitaJpaRepository extends JpaRepository<CitaEntity, String> {
    
    /**
     * Busca citas por estado
     */
    List<CitaEntity> findByEstado(EstadoCita estado);
    
    /**
     * Busca citas por tipo y estado
     */
    List<CitaEntity> findByTipoCitaAndEstado(TipoCita tipoCita, EstadoCita estado);
    
    /**
     * Busca citas por NIT del proveedor
     */
    List<CitaEntity> findByNit(String nit);
    
    /**
     * Busca citas en un rango de fechas
     */
    List<CitaEntity> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca citas por fecha específica (usando consulta personalizada)
     */
    @Query("SELECT c FROM CitaEntity c WHERE DATE(c.fechaHora) = :fecha")
    List<CitaEntity> findByFecha(@Param("fecha") LocalDate fecha);
    
    /**
     * Busca citas por tipo en una fecha específica
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.tipoCita = :tipo AND DATE(c.fechaHora) = :fecha")
    List<CitaEntity> findByTipoCitaAndFecha(@Param("tipo") TipoCita tipo, @Param("fecha") LocalDate fecha);
    
    /**
     * Busca citas activas (PENDIENTE o CONFIRMADA) por tipo
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.tipoCita = :tipo AND c.estado IN ('PENDIENTE', 'CONFIRMADA')")
    List<CitaEntity> findActivasByTipoCita(@Param("tipo") TipoCita tipo);
    
    /**
     * Busca citas que tienen conflicto de horario
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.tipoCita = :tipo AND c.fechaHora = :fechaHora AND c.estado IN ('PENDIENTE', 'CONFIRMADA')")
    List<CitaEntity> findConflictoHorario(@Param("tipo") TipoCita tipo, @Param("fechaHora") LocalDateTime fechaHora);
    
    /**
     * Cuenta citas por estado
     */
    long countByEstado(EstadoCita estado);
    
    /**
     * Cuenta citas por tipo y estado
     */
    long countByTipoCitaAndEstado(TipoCita tipoCita, EstadoCita estado);
    
    /**
     * Busca citas por estado ordenadas por fecha de creación
     */
    List<CitaEntity> findByEstadoOrderByFechaCreacionDesc(EstadoCita estado);
    
    /**
     * Busca citas por tipo ordenadas por fecha y hora
     */
    List<CitaEntity> findByTipoCitaOrderByFechaHoraAsc(TipoCita tipoCita);
    
    /**
     * Busca citas creadas en las últimas 24 horas
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.fechaCreacion >= :fecha")
    List<CitaEntity> findCitasRecientes(@Param("fecha") LocalDateTime fecha);
    
    /**
     * Busca citas próximas (en las próximas 24 horas)
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.fechaHora BETWEEN :ahora AND :limite AND c.estado = 'CONFIRMADA'")
    List<CitaEntity> findCitasProximas(@Param("ahora") LocalDateTime ahora, @Param("limite") LocalDateTime limite);
    
    /**
     * Busca citas vencidas (pasadas y aún pendientes)
     */
    @Query("SELECT c FROM CitaEntity c WHERE c.fechaHora < :ahora AND c.estado = 'PENDIENTE'")
    List<CitaEntity> findCitasVencidas(@Param("ahora") LocalDateTime ahora);
}