package com.tayronadev.dominio.citas.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Value Object que representa el horario de una cita
 */
@Getter
@EqualsAndHashCode
@ToString
public final class Horario {
    
    @NonNull 
    private final LocalDateTime fechaHora;
    
    /**
     * Constructor para crear un nuevo horario (valida que no sea en el pasado)
     */
    public Horario(@NonNull LocalDateTime fechaHora) {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en el pasado");
        }
        this.fechaHora = fechaHora;
    }
    
    /**
     * Constructor privado para reconstrucción sin validación
     */
    private Horario(@NonNull LocalDateTime fechaHora, boolean esReconstruccion) {
        this.fechaHora = fechaHora;
    }
    
    /**
     * Factory method para reconstruir un horario desde persistencia (sin validación de fecha pasada)
     */
    public static Horario reconstruir(@NonNull LocalDateTime fechaHora) {
        return new Horario(fechaHora, true);
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
