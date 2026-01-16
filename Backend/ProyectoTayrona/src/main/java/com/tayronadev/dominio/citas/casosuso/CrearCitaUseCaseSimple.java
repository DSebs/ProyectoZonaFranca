package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.ValidadorHorarios;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear una nueva cita (versión simplificada)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CrearCitaUseCaseSimple {
    
    private final CitaRepositorio citaRepositorio;
    private final ValidadorHorarios validadorHorarios;
    
    /**
     * Crea una nueva cita validando disponibilidad de horario
     */
    public Cita ejecutar(TipoCita tipoCita,
                        InformacionProveedor proveedor,
                        OpcionTransporte transporte,
                        Horario horario) {
        
        // Validar disponibilidad del horario
        var citasExistentes = citaRepositorio.buscarActivasPorTipo(tipoCita);
        validadorHorarios.validarDisponibilidad(horario, tipoCita, citasExistentes);
        
        // Crear y guardar la nueva cita
        var nuevaCita = new Cita(tipoCita, proveedor, transporte, horario);
        return citaRepositorio.guardar(nuevaCita);
    }
}