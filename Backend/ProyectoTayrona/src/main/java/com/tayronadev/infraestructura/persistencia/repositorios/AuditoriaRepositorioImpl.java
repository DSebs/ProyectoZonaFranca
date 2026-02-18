package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.auditoria.modelo.RegistroCambioEstado;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import com.tayronadev.dominio.auditoria.repositorios.AuditoriaRepositorio;
import com.tayronadev.infraestructura.persistencia.mappers.AuditoriaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de auditoría usando JPA.
 * Actúa como adaptador entre el puerto (interfaz de dominio) y Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AuditoriaRepositorioImpl implements AuditoriaRepositorio {
    
    private final AuditoriaJpaRepository jpaRepository;
    private final AuditoriaMapper mapper;
    
    @Override
    public RegistroCambioEstado guardar(RegistroCambioEstado registro) {
        log.debug("Guardando registro de auditoría para cita: {}", registro.getCitaId());
        var entity = mapper.toEntity(registro);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<RegistroCambioEstado> buscarPorId(String id) {
        log.debug("Buscando registro de auditoría por ID: {}", id);
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public List<RegistroCambioEstado> buscarPorCitaId(String citaId) {
        log.debug("Buscando registros de auditoría para cita: {}", citaId);
        return jpaRepository.findByCitaIdOrderByFechaCambioDesc(citaId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<RegistroCambioEstado> buscarPorUsuarioId(String usuarioId) {
        log.debug("Buscando registros de auditoría para usuario: {}", usuarioId);
        return jpaRepository.findByUsuarioIdOrderByFechaCambioDesc(usuarioId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<RegistroCambioEstado> buscarPorTipoCambio(TipoCambio tipoCambio) {
        log.debug("Buscando registros de auditoría por tipo de cambio: {}", tipoCambio);
        return jpaRepository.findByTipoCambioOrderByFechaCambioDesc(tipoCambio)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<RegistroCambioEstado> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        log.debug("Buscando registros de auditoría entre {} y {}", desde, hasta);
        return jpaRepository.findByFechaCambioBetweenOrderByFechaCambioDesc(desde, hasta)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<RegistroCambioEstado> obtenerUltimosRegistros(int limite) {
        log.debug("Obteniendo últimos {} registros de auditoría", limite);
        return jpaRepository.findTop100ByOrderByFechaCambioDesc()
            .stream()
            .limit(limite)
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public long contarPorUsuarioId(String usuarioId) {
        return jpaRepository.countByUsuarioId(usuarioId);
    }
    
    @Override
    public long contarPorCitaId(String citaId) {
        return jpaRepository.countByCitaId(citaId);
    }
}
