package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.infraestructura.persistencia.mappers.CitaMapperSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación simplificada del repositorio de citas usando JPA
 */
@Repository
@RequiredArgsConstructor
public class CitaRepositorioSimple implements CitaRepositorio {
    
    private final CitaJpaRepository jpaRepository;
    private final CitaMapperSimple mapper;
    
    @Override
    public Cita guardar(Cita cita) {
        var entity = mapper.toEntity(cita);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Cita> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Cita> buscarPorEstado(EstadoCita estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorTipoYEstado(TipoCita tipo, EstadoCita estado) {
        return jpaRepository.findByTipoCitaAndEstado(tipo, estado)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorNitProveedor(String nit) {
        return jpaRepository.findByNit(nit)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaRepository.findByFechaHoraBetween(fechaInicio, fechaFin)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        return jpaRepository.findByFecha(fecha)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarPorTipoYFecha(TipoCita tipo, LocalDate fecha) {
        return jpaRepository.findByTipoCitaAndFecha(tipo, fecha)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarActivasPorTipo(TipoCita tipo) {
        return jpaRepository.findActivasByTipoCita(tipo)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> buscarConflictoHorario(TipoCita tipo, LocalDateTime fechaHora) {
        return jpaRepository.findConflictoHorario(tipo, fechaHora)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public long contarPorEstado(EstadoCita estado) {
        return jpaRepository.countByEstado(estado);
    }
    
    @Override
    public long contarPorTipoYEstado(TipoCita tipo, EstadoCita estado) {
        return jpaRepository.countByTipoCitaAndEstado(tipo, estado);
    }
    
    @Override
    public boolean existePorId(String id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public void eliminar(String id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public List<Cita> obtenerTodas() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}