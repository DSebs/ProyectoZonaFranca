package com.tayronadev.api.citas.controllers;

import com.tayronadev.api.citas.dto.response.HorarioDisponibleResponse;
import com.tayronadev.api.citas.dto.response.HorarioDisponibleResponse.HorarioSlot;
import com.tayronadev.api.citas.dto.response.TipoCitaResponse;
import com.tayronadev.api.common.ApiResponse;
import com.tayronadev.dominio.citas.casosuso.ConsultarCitasUseCase;
import com.tayronadev.dominio.citas.casosuso.CrearCitaUseCase;
import com.tayronadev.dominio.citas.modelo.Horario;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import com.tayronadev.dominio.citas.servicios.ValidadorHorarios;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador REST para consultar disponibilidad de horarios.
 * Este controlador es público para que los proveedores puedan ver los horarios disponibles.
 */
@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
@Slf4j
public class HorarioController {
    
    private final ValidadorHorarios validadorHorarios;
    private final ConsultarCitasUseCase consultarCitasUseCase;
    private final CrearCitaUseCase crearCitaUseCase;
    
    /**
     * Obtiene los tipos de cita disponibles con sus horarios permitidos
     */
    @GetMapping("/tipos-cita")
    public ResponseEntity<ApiResponse<List<TipoCitaResponse>>> obtenerTiposCita() {
        log.debug("Consultando tipos de cita disponibles");
        
        var tipos = Arrays.stream(TipoCita.values())
                .map(tipo -> TipoCitaResponse.builder()
                        .codigo(tipo.name())
                        .descripcion(tipo.getDescripcion())
                        .horariosPermitidos(new ArrayList<>(validadorHorarios.obtenerHorariosDisponibles(tipo)))
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(tipos));
    }
    
    /**
     * Obtiene los horarios disponibles para un tipo de cita en una fecha específica
     */
    @GetMapping("/disponibles/{tipoCita}/{fecha}")
    public ResponseEntity<ApiResponse<HorarioDisponibleResponse>> obtenerHorariosDisponibles(
            @PathVariable TipoCita tipoCita,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        log.debug("Consultando horarios disponibles para tipo: {} en fecha: {}", tipoCita, fecha);
        
        // Validar que no sea fin de semana
        if (esFindeSemana(fecha)) {
            var response = HorarioDisponibleResponse.builder()
                    .tipoCita(tipoCita.name())
                    .tipoCitaDescripcion(tipoCita.getDescripcion())
                    .fecha(fecha)
                    .horariosDisponibles(Collections.emptyList())
                    .horariosOcupados(Collections.emptyList())
                    .build();
            return ResponseEntity.ok(ApiResponse.success(response, "No hay horarios disponibles en fines de semana"));
        }
        
        // Obtener horarios permitidos para este tipo de cita
        Set<Integer> horariosPermitidos = validadorHorarios.obtenerHorariosDisponibles(tipoCita);
        
        // Obtener citas existentes para este tipo y fecha
        var citasExistentes = consultarCitasUseCase.buscarPorTipoYFecha(tipoCita, fecha);
        
        // Determinar qué horarios ya están ocupados
        Set<Integer> horariosOcupados = citasExistentes.stream()
                .filter(cita -> !cita.getEstado().esFinal()) // Solo citas activas
                .map(cita -> cita.getHorario().getFechaHora().getHour())
                .collect(Collectors.toSet());
        
        // Construir lista de slots disponibles y ocupados
        List<HorarioSlot> slotsDisponibles = new ArrayList<>();
        List<HorarioSlot> slotsOcupados = new ArrayList<>();
        
        for (Integer hora : horariosPermitidos) {
            boolean disponible = !horariosOcupados.contains(hora);
            
            // Verificar si el horario ya pasó (para la fecha de hoy)
            if (fecha.equals(LocalDate.now()) && hora <= LocalDateTime.now().getHour()) {
                disponible = false;
            }
            
            var slot = HorarioSlot.builder()
                    .hora(hora)
                    .horaFormateada(String.format("%02d:00", hora))
                    .disponible(disponible)
                    .build();
            
            if (disponible) {
                slotsDisponibles.add(slot);
            } else {
                slotsOcupados.add(slot);
            }
        }
        
        var response = HorarioDisponibleResponse.builder()
                .tipoCita(tipoCita.name())
                .tipoCitaDescripcion(tipoCita.getDescripcion())
                .fecha(fecha)
                .horariosDisponibles(slotsDisponibles)
                .horariosOcupados(slotsOcupados)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Verifica si un horario específico está disponible
     */
    @GetMapping("/verificar/{tipoCita}/{fechaHora}")
    public ResponseEntity<ApiResponse<Boolean>> verificarDisponibilidad(
            @PathVariable TipoCita tipoCita,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora) {
        
        log.debug("Verificando disponibilidad para tipo: {} en: {}", tipoCita, fechaHora);
        
        try {
            var horario = new Horario(fechaHora);
            boolean disponible = crearCitaUseCase.validarDisponibilidadHorario(tipoCita, horario);
            
            return ResponseEntity.ok(ApiResponse.success(disponible, 
                    disponible ? "Horario disponible" : "Horario no disponible"));
        } catch (IllegalArgumentException e) {
            // Si el horario está en el pasado o fuera de horario laboral
            return ResponseEntity.ok(ApiResponse.success(false, e.getMessage()));
        }
    }
    
    /**
     * Obtiene los horarios permitidos para un tipo de cita
     */
    @GetMapping("/permitidos/{tipoCita}")
    public ResponseEntity<ApiResponse<List<Integer>>> obtenerHorariosPermitidos(
            @PathVariable TipoCita tipoCita) {
        
        log.debug("Consultando horarios permitidos para tipo: {}", tipoCita);
        
        Set<Integer> horarios = validadorHorarios.obtenerHorariosDisponibles(tipoCita);
        
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(horarios)));
    }
    
    /**
     * Verifica si una fecha es fin de semana
     */
    private boolean esFindeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }
}
