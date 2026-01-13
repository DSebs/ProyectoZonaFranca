package com.tayronadev.dominio.citas.modelo.excepciones;

import com.tayronadev.dominio.citas.modelo.Horario;
import com.tayronadev.dominio.citas.modelo.TipoCita;

/**
 * Excepción lanzada cuando se intenta agendar una cita en un horario no disponible
 */
public class HorarioNoDisponibleException extends CitaException {
    
    private final Horario horario;
    private final TipoCita tipoCita;
    
    public HorarioNoDisponibleException(Horario horario, TipoCita tipoCita) {
        super(String.format("El horario %s no está disponible para citas de tipo %s", 
              horario.fechaHora(), tipoCita.getDescripcion()));
        this.horario = horario;
        this.tipoCita = tipoCita;
    }
    
    public Horario getHorario() {
        return horario;
    }
    
    public TipoCita getTipoCita() {
        return tipoCita;
    }
}