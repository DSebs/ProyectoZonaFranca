package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.HorarioNoDisponibleException;
import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.ValidadorHorarios;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear una nueva cita
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CrearCitaUseCase {
    
    private final CitaRepositorio citaRepositorio;
    private final ValidadorHorarios validadorHorarios;
    
    /**
     * Crea una nueva cita validando disponibilidad de horario
     */
    public Cita ejecutar(TipoCita tipoCita,
                        InformacionProveedor proveedor,
                        OpcionTransporte transporte,
                        Horario horario) {
        
        log.info("Iniciando creación de cita para proveedor {} - Tipo: {} - Horario: {}", 
                proveedor.getNombreProveedor(), tipoCita, horario.getFechaHora());
        
        // Validar disponibilidad del horario
        var citasExistentes = citaRepositorio.buscarActivasPorTipo(tipoCita);
        validadorHorarios.validarDisponibilidad(horario, tipoCita, citasExistentes);
        
        // Crear la nueva cita
        var nuevaCita = new Cita(tipoCita, proveedor, transporte, horario);
        
        // Guardar en repositorio
        var citaGuardada = citaRepositorio.guardar(nuevaCita);
        
        log.info("Cita creada exitosamente con ID: {} para proveedor: {}", 
                citaGuardada.getId(), proveedor.getNombreProveedor());
        
        return citaGuardada;
    }
    
    /**
     * Valida si un horario está disponible sin crear la cita
     */
    @Transactional(readOnly = true)
    public boolean validarDisponibilidadHorario(TipoCita tipoCita, Horario horario) {
        try {
            var citasExistentes = citaRepositorio.buscarActivasPorTipo(tipoCita);
            validadorHorarios.validarDisponibilidad(horario, tipoCita, citasExistentes);
            return true;
        } catch (HorarioNoDisponibleException e) {
            log.debug("Horario no disponible: {}", e.getMessage());
            return false;
        }
    }
}