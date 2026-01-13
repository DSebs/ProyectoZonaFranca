package com.tayronadev.dominio.citas.modelo.servicios;

import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.modelo.excepciones.HorarioNoDisponibleException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Servicio de dominio para validar la disponibilidad de horarios según el tipo de cita
 */
public class ValidadorHorarios {
    
    // Horarios permitidos por tipo de cita (esto podría venir de configuración)
    private static final Set<Integer> HORARIOS_ENTREGA = Set.of(8, 9, 10, 11, 14, 15, 16);
    private static final Set<Integer> HORARIOS_RECOJO = Set.of(9, 10, 11, 14, 15, 16, 17);
    private static final Set<Integer> HORARIOS_IMPORTACION = Set.of(8, 9, 10, 11);
    private static final Set<Integer> HORARIOS_DEVOLUCION = Set.of(14, 15, 16, 17);
    
    /**
     * Valida si un horario está disponible para un tipo de cita específico
     */
    public void validarDisponibilidad(Horario horario, TipoCita tipoCita, List<Cita> citasExistentes) {
        // Validar que el horario esté permitido para el tipo de cita
        if (!esHorarioPermitidoParaTipo(horario, tipoCita)) {
            throw new HorarioNoDisponibleException(horario, tipoCita);
        }
        
        // Validar que no sea fin de semana
        if (esFindeSemana(horario.fechaHora())) {
            throw new HorarioNoDisponibleException(horario, tipoCita);
        }
        
        // Validar que no haya conflictos con citas existentes del mismo tipo
        if (tieneConflictoConCitasExistentes(horario, tipoCita, citasExistentes)) {
            throw new HorarioNoDisponibleException(horario, tipoCita);
        }
    }
    
    /**
     * Verifica si un horario está permitido para un tipo de cita específico
     */
    public boolean esHorarioPermitidoParaTipo(Horario horario, TipoCita tipoCita) {
        int hora = horario.fechaHora().getHour();
        
        return switch (tipoCita) {
            case ENTREGA -> HORARIOS_ENTREGA.contains(hora);
            case RECOJO -> HORARIOS_RECOJO.contains(hora);
            case IMPORTACION -> HORARIOS_IMPORTACION.contains(hora);
            case DEVOLUCION -> HORARIOS_DEVOLUCION.contains(hora);
        };
    }
    
    /**
     * Obtiene los horarios disponibles para un tipo de cita en una fecha específica
     */
    public Set<Integer> obtenerHorariosDisponibles(TipoCita tipoCita) {
        return switch (tipoCita) {
            case ENTREGA -> HORARIOS_ENTREGA;
            case RECOJO -> HORARIOS_RECOJO;
            case IMPORTACION -> HORARIOS_IMPORTACION;
            case DEVOLUCION -> HORARIOS_DEVOLUCION;
        };
    }
    
    private boolean esFindeSemana(LocalDateTime fechaHora) {
        DayOfWeek dia = fechaHora.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }
    
    private boolean tieneConflictoConCitasExistentes(Horario horario, TipoCita tipoCita, List<Cita> citasExistentes) {
        return citasExistentes.stream()
                .filter(cita -> cita.getTipoCita() == tipoCita)
                .filter(cita -> cita.getEstado() == EstadoCita.CONFIRMADA || cita.getEstado() == EstadoCita.PENDIENTE)
                .anyMatch(cita -> cita.getHorario().conflictoCon(horario));
    }
}