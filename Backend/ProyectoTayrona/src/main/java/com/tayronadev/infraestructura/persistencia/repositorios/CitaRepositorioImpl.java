package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.infraestructura.persistencia.mappers.CitaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del repositorio de citas usando JPA
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CitaRepositorioImpl implements CitaRepositorio {
    
    private final CitaJpaRepository jpaRepository;
    private final CitaMapper mapper;
    
    @Override
    public Cita guardar(Cita cita) {
        log.debug("Guardando cita con ID: {}", cita.getId());
        var entity = mapper.toEntity(cita);
        var savedEntity = jpaRepository.save(entity);
        var result = mapper.toDomain(savedEntity);
        log.debug("Cita guardada exitosamente con ID: {}", result.getId());
        return result;
    }
    
    @Override
    public Optional<Cita> buscarPorId(String id) {
        log.debug("Buscando cita por ID: {}", id);
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Cita> buscarPorEstado(EstadoCita estado) {
        log.debug("Buscando citas por estado: {}", estado);
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorTipoYEstado(TipoCita tipo, EstadoCita estado) {
        log.debug("Buscando citas por tipo: {} y estado: {}", tipo, estado);
        return jpaRepository.findByTipoCitaAndEstado(tipo, estado)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorNitProveedor(String nit) {
        log.debug("Buscando citas por NIT del proveedor: {}", nit);
        return jpaRepository.findByNit(nit)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Buscando citas entre {} y {}", fechaInicio, fechaFin);
        return jpaRepository.findByFechaHoraBetween(fechaInicio, fechaFin)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        log.debug("Buscando citas para la fecha: {}", fecha);
        return jpaRepository.findByFecha(fecha)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorTipoYFecha(TipoCita tipo, LocalDate fecha) {
        log.debug("Buscando citas de tipo: {} para la fecha: {}", tipo, fecha);
        return jpaRepository.findByTipoCitaAndFecha(tipo, fecha)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarActivasPorTipo(TipoCita tipo) {
        log.debug("Buscando citas activas de tipo: {}", tipo);
        return jpaRepository.findActivasByTipoCita(tipo)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarConflictoHorario(TipoCita tipo, LocalDateTime fechaHora) {
        log.debug("Buscando conflictos de horario para tipo: {} en: {}", tipo, fechaHora);
        return jpaRepository.findConflictoHorario(tipo, fechaHora)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public long contarPorEstado(EstadoCita estado) {
        log.debug("Contando citas por estado: {}", estado);
        return jpaRepository.countByEstado(estado);
    }
    
    @Override
    public long contarPorTipoYEstado(TipoCita tipo, EstadoCita estado) {
        log.debug("Contando citas por tipo: {} y estado: {}", tipo, estado);
        return jpaRepository.countByTipoCitaAndEstado(tipo, estado);
    }
    
    @Override
    public boolean existePorId(String id) {
        log.debug("Verificando existencia de cita con ID: {}", id);
        return jpaRepository.existsById(id);
    }
    
    @Override
    public void eliminar(String id) {
        log.warn("Eliminando cita con ID: {}", id);
        jpaRepository.deleteById(id);
    }
    
    @Override
    public List<Cita> obtenerTodas() {
        log.debug("Obteniendo todas las citas");
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Métodos adicionales específicos de la implementación
     */
    
    public List<Cita> buscarCitasRecientes() {
        var hace24Horas = LocalDateTime.now().minusHours(24);
        return jpaRepository.findCitasRecientes(hace24Horas)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    public List<Cita> buscarCitasProximas() {
        var ahora = LocalDateTime.now();
        var en24Horas = ahora.plusHours(24);
        return jpaRepository.findCitasProximas(ahora, en24Horas)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    public List<Cita> buscarCitasVencidas() {
        var ahora = LocalDateTime.now();
        return jpaRepository.findCitasVencidas(ahora)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}