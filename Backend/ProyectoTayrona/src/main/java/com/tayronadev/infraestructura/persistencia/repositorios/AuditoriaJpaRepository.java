package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import com.tayronadev.infraestructura.persistencia.entidades.RegistroAuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad de auditoría.
 */
@Repository
public interface AuditoriaJpaRepository extends JpaRepository<RegistroAuditoriaEntity, String> {
    
    /**
     * Busca registros por ID de cita, ordenados por fecha descendente
     */
    List<RegistroAuditoriaEntity> findByCitaIdOrderByFechaCambioDesc(String citaId);
    
    /**
     * Busca registros por ID de usuario, ordenados por fecha descendente
     */
    List<RegistroAuditoriaEntity> findByUsuarioIdOrderByFechaCambioDesc(String usuarioId);
    
    /**
     * Busca registros por tipo de cambio, ordenados por fecha descendente
     */
    List<RegistroAuditoriaEntity> findByTipoCambioOrderByFechaCambioDesc(TipoCambio tipoCambio);
    
    /**
     * Busca registros en un rango de fechas, ordenados por fecha descendente
     */
    List<RegistroAuditoriaEntity> findByFechaCambioBetweenOrderByFechaCambioDesc(
        LocalDateTime desde, LocalDateTime hasta);
    
    /**
     * Obtiene los últimos N registros ordenados por fecha descendente
     */
    List<RegistroAuditoriaEntity> findTop100ByOrderByFechaCambioDesc();
    
    /**
     * Cuenta registros por usuario
     */
    long countByUsuarioId(String usuarioId);
    
    /**
     * Cuenta registros por cita
     */
    long countByCitaId(String citaId);
}
