package com.tayronadev.dominio.citas.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object que representa el horario de una cita
 */
public record Horario(LocalDateTime fechaHora) {
    
    public Horario {
        Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias");
        
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en el pasado");
        }
    }
    
    /**
     * Verifica si este horario está en conflicto con otro
     */
    public boolean conflictoCon(Horario otroHorario) {
        if (otroHorario == null) return false;
        
        // Consideramos conflicto si están en la misma hora
        // En el futuro se puede ajustar según las reglas de negocio específicas
        return this.fechaHora.equals(otroHorario.fechaHora);
    }
    
    /**
     * Verifica si el horario está dentro del rango de horarios laborales
     */
    public boolean esDentroDeHorarioLaboral() {
        int hora = fechaHora.getHour();
        // Asumiendo horario laboral de 8 AM a 6 PM
        return hora >= 8 && hora <= 18;
    }
}