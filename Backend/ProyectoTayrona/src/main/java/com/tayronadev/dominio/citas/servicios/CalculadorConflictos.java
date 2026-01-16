package com.tayronadev.dominio.citas.servicios;

import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.Horario;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para calcular conflictos entre citas
 */
@Service
@Slf4j
public class CalculadorConflictos {
    
    /**
     * Encuentra todas las citas que tienen conflicto de horario con una nueva cita propuesta
     */
    public List<Cita> encontrarConflictos(TipoCita tipoCita, Horario horario, List<Cita> citasExistentes) {
        return citasExistentes.stream()
                .filter(cita -> cita.getTipoCita() == tipoCita)
                .filter(cita -> !cita.getEstado().esFinal())
                .filter(cita -> cita.getHorario().conflictoCon(horario))
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica si existe algún conflicto para una nueva cita
     */
    public boolean existeConflicto(TipoCita tipoCita, Horario horario, List<Cita> citasExistentes) {
        return !encontrarConflictos(tipoCita, horario, citasExistentes).isEmpty();
    }
    
    /**
     * Cuenta el número de citas activas (pendientes o confirmadas) para un tipo específico
     */
    public long contarCitasActivas(TipoCita tipoCita, List<Cita> todasLasCitas) {
        return todasLasCitas.stream()
                .filter(cita -> cita.getTipoCita() == tipoCita)
                .filter(cita -> cita.getEstado() == EstadoCita.PENDIENTE || 
                              cita.getEstado() == EstadoCita.CONFIRMADA)
                .count();
    }
    
    /**
     * Obtiene todas las citas activas para un proveedor específico
     */
    public List<Cita> obtenerCitasActivasProveedor(String nitProveedor, List<Cita> todasLasCitas) {
        return todasLasCitas.stream()
                .filter(cita -> cita.getProveedor().getNit().equals(nitProveedor))
                .filter(cita -> !cita.getEstado().esFinal())
                .collect(Collectors.toList());
    }
}