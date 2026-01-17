package com.tayronadev.api.citas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de respuesta para los horarios disponibles
 */
@Value
@Builder
public class HorarioDisponibleResponse {
    
    String tipoCita;
    String tipoCitaDescripcion;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate fecha;
    
    List<HorarioSlot> horariosDisponibles;
    List<HorarioSlot> horariosOcupados;
    
    /**
     * Representa un slot de horario
     */
    @Value
    @Builder
    public static class HorarioSlot {
        Integer hora;
        String horaFormateada; // Ej: "08:00", "14:00"
        Boolean disponible;
    }
}
