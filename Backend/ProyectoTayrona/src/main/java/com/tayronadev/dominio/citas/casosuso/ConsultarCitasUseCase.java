package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.CalculadorConflictos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso para consultar citas
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ConsultarCitasUseCase {
    
    private final CitaRepositorio citaRepositorio;
    private final CalculadorConflictos calculadorConflictos;
    
    /**
     * Busca una cita por su ID
     */
    public Cita buscarPorId(String citaId) {
        log.debug("Buscando cita con ID: {}", citaId);
        return citaRepositorio.buscarPorId(citaId)
                .orElseThrow(() -> new CitaNoEncontradaException(citaId));
    }
    
    /**
     * Obtiene todas las citas por estado
     */
    public List<Cita> buscarPorEstado(EstadoCita estado) {
        log.debug("Buscando citas por estado: {}", estado);
        return citaRepositorio.buscarPorEstado(estado);
    }
    
    /**
     * Obtiene citas por tipo y estado
     */
    public List<Cita> buscarPorTipoYEstado(TipoCita tipo, EstadoCita estado) {
        log.debug("Buscando citas por tipo: {} y estado: {}", tipo, estado);
        return citaRepositorio.buscarPorTipoYEstado(tipo, estado);
    }
    
    /**
     * Obtiene todas las citas de un proveedor por NIT
     */
    public List<Cita> buscarPorProveedor(String nit) {
        log.debug("Buscando citas del proveedor con NIT: {}", nit);
        return citaRepositorio.buscarPorNitProveedor(nit);
    }
    
    /**
     * Obtiene citas activas de un proveedor
     */
    public List<Cita> buscarCitasActivasProveedor(String nit) {
        log.debug("Buscando citas activas del proveedor con NIT: {}", nit);
        var todasLasCitas = citaRepositorio.obtenerTodas();
        return calculadorConflictos.obtenerCitasActivasProveedor(nit, todasLasCitas);
    }
    
    /**
     * Obtiene citas por fecha específica
     */
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        log.debug("Buscando citas para la fecha: {}", fecha);
        return citaRepositorio.buscarPorFecha(fecha);
    }
    
    /**
     * Obtiene citas por tipo en una fecha específica
     */
    public List<Cita> buscarPorTipoYFecha(TipoCita tipo, LocalDate fecha) {
        log.debug("Buscando citas de tipo: {} para la fecha: {}", tipo, fecha);
        return citaRepositorio.buscarPorTipoYFecha(tipo, fecha);
    }
    
    /**
     * Obtiene citas en un rango de fechas
     */
    public List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Buscando citas entre {} y {}", fechaInicio, fechaFin);
        return citaRepositorio.buscarPorRangoFechas(fechaInicio, fechaFin);
    }
    
    /**
     * Obtiene todas las citas pendientes
     */
    public List<Cita> obtenerCitasPendientes() {
        log.debug("Obteniendo todas las citas pendientes");
        return citaRepositorio.buscarPorEstado(EstadoCita.PENDIENTE);
    }
    
    /**
     * Obtiene todas las citas confirmadas
     */
    public List<Cita> obtenerCitasConfirmadas() {
        log.debug("Obteniendo todas las citas confirmadas");
        return citaRepositorio.buscarPorEstado(EstadoCita.CONFIRMADA);
    }
    
    /**
     * Obtiene citas activas por tipo
     */
    public List<Cita> obtenerCitasActivasPorTipo(TipoCita tipo) {
        log.debug("Obteniendo citas activas de tipo: {}", tipo);
        return citaRepositorio.buscarActivasPorTipo(tipo);
    }
    
    /**
     * Cuenta citas por estado
     */
    public long contarPorEstado(EstadoCita estado) {
        log.debug("Contando citas por estado: {}", estado);
        return citaRepositorio.contarPorEstado(estado);
    }
    
    /**
     * Cuenta citas activas por tipo
     */
    public long contarCitasActivasPorTipo(TipoCita tipo) {
        log.debug("Contando citas activas de tipo: {}", tipo);
        var todasLasCitas = citaRepositorio.obtenerTodas();
        return calculadorConflictos.contarCitasActivas(tipo, todasLasCitas);
    }
    
    /**
     * Busca conflictos de horario para un tipo y hora específica
     */
    public List<Cita> buscarConflictosHorario(TipoCita tipo, LocalDateTime fechaHora) {
        log.debug("Buscando conflictos para tipo: {} en horario: {}", tipo, fechaHora);
        return citaRepositorio.buscarConflictoHorario(tipo, fechaHora);
    }
    
    /**
     * Verifica si existe una cita con el ID especificado
     */
    public boolean existeCita(String citaId) {
        return citaRepositorio.existePorId(citaId);
    }
}