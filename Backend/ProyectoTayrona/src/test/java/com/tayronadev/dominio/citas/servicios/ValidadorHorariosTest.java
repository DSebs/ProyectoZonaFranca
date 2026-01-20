package com.tayronadev.dominio.citas.servicios;

import com.tayronadev.dominio.citas.excepciones.HorarioNoDisponibleException;
import com.tayronadev.dominio.citas.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidadorHorarios - Servicio de Dominio")
class ValidadorHorariosTest {
    
    private ValidadorHorarios validadorHorarios;
    
    @BeforeEach
    void setUp() {
        validadorHorarios = new ValidadorHorarios();
    }
    
    @Nested
    @DisplayName("Horarios permitidos por tipo de cita")
    class HorariosPermitidosPorTipo {
        
        @ParameterizedTest
        @ValueSource(ints = {8, 9, 10, 11, 14, 15, 16})
        @DisplayName("ENTREGA permite horarios específicos")
        void entregaPermiteHorariosEspecificos(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertTrue(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.ENTREGA));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {7, 12, 13, 17, 18, 19})
        @DisplayName("ENTREGA NO permite horarios fuera del rango")
        void entregaNoPermiteHorariosFueraDelRango(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertFalse(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.ENTREGA));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {9, 10, 11, 14, 15, 16, 17})
        @DisplayName("RECOJO permite horarios específicos")
        void recojoPermiteHorariosEspecificos(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertTrue(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.RECOJO));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {8, 9, 10, 11})
        @DisplayName("IMPORTACION permite horarios específicos")
        void importacionPermiteHorariosEspecificos(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertTrue(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.IMPORTACION));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {12, 13, 14, 15, 16, 17})
        @DisplayName("IMPORTACION NO permite horarios de la tarde")
        void importacionNoPermiteHorariosTarde(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertFalse(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.IMPORTACION));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {14, 15, 16, 17})
        @DisplayName("DEVOLUCION permite horarios específicos")
        void devolucionPermiteHorariosEspecificos(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertTrue(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.DEVOLUCION));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {8, 9, 10, 11, 12, 13})
        @DisplayName("DEVOLUCION NO permite horarios de la mañana")
        void devolucionNoPermiteHorariosMañana(int hora) {
            var horario = crearHorarioEnDiaLaboral(hora);
            
            assertFalse(validadorHorarios.esHorarioPermitidoParaTipo(horario, TipoCita.DEVOLUCION));
        }
    }
    
    @Nested
    @DisplayName("Obtener horarios disponibles")
    class ObtenerHorariosDisponibles {
        
        @Test
        @DisplayName("Debe retornar horarios correctos para ENTREGA")
        void debeRetornarHorariosCorrectosParaEntrega() {
            Set<Integer> horarios = validadorHorarios.obtenerHorariosDisponibles(TipoCita.ENTREGA);
            
            assertEquals(Set.of(8, 9, 10, 11, 14, 15, 16), horarios);
        }
        
        @Test
        @DisplayName("Debe retornar horarios correctos para RECOJO")
        void debeRetornarHorariosCorrectosParaRecojo() {
            Set<Integer> horarios = validadorHorarios.obtenerHorariosDisponibles(TipoCita.RECOJO);
            
            assertEquals(Set.of(9, 10, 11, 14, 15, 16, 17), horarios);
        }
        
        @Test
        @DisplayName("Debe retornar horarios correctos para IMPORTACION")
        void debeRetornarHorariosCorrectosParaImportacion() {
            Set<Integer> horarios = validadorHorarios.obtenerHorariosDisponibles(TipoCita.IMPORTACION);
            
            assertEquals(Set.of(8, 9, 10, 11), horarios);
        }
        
        @Test
        @DisplayName("Debe retornar horarios correctos para DEVOLUCION")
        void debeRetornarHorariosCorrectosParaDevolucion() {
            Set<Integer> horarios = validadorHorarios.obtenerHorariosDisponibles(TipoCita.DEVOLUCION);
            
            assertEquals(Set.of(14, 15, 16, 17), horarios);
        }
    }
    
    @Nested
    @DisplayName("Validación de disponibilidad")
    class ValidacionDisponibilidad {
        
        @Test
        @DisplayName("Debe aceptar horario válido sin conflictos")
        void debeAceptarHorarioValidoSinConflictos() {
            var horario = crearHorarioEnDiaLaboral(10);
            
            assertDoesNotThrow(() -> 
                    validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, Collections.emptyList()));
        }
        
        @Test
        @DisplayName("Debe rechazar horario no permitido para el tipo")
        void debeRechazarHorarioNoPermitido() {
            var horario = crearHorarioEnDiaLaboral(12); // 12 no está permitido para ENTREGA
            
            assertThrows(HorarioNoDisponibleException.class, 
                    () -> validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, Collections.emptyList()));
        }
        
        @Test
        @DisplayName("Debe rechazar horario en fin de semana")
        void debeRechazarHorarioEnFinDeSemana() {
            var sabado = obtenerProximoSabado().withHour(10);
            var horario = Horario.reconstruir(sabado);
            
            assertThrows(HorarioNoDisponibleException.class, 
                    () -> validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, Collections.emptyList()));
        }
        
        @Test
        @DisplayName("Debe rechazar horario con conflicto de cita existente")
        void debeRechazarHorarioConConflicto() {
            var fechaHora = obtenerProximoDiaLaboral().withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario = Horario.reconstruir(fechaHora);
            
            // Crear cita existente con el mismo horario y tipo
            var citaExistente = crearCitaConHorarioYTipo(fechaHora, TipoCita.ENTREGA, EstadoCita.PENDIENTE);
            
            assertThrows(HorarioNoDisponibleException.class, 
                    () -> validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, List.of(citaExistente)));
        }
        
        @Test
        @DisplayName("No debe haber conflicto con cita de diferente tipo")
        void noDebeHaberConflictoConDiferenteTipo() {
            var fechaHora = obtenerProximoDiaLaboral().withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario = Horario.reconstruir(fechaHora);
            
            // Cita existente de tipo RECOJO
            var citaExistente = crearCitaConHorarioYTipo(fechaHora, TipoCita.RECOJO, EstadoCita.PENDIENTE);
            
            assertDoesNotThrow(() -> 
                    validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, List.of(citaExistente)));
        }
        
        @Test
        @DisplayName("No debe haber conflicto con cita cancelada")
        void noDebeHaberConflictoConCitaCancelada() {
            var fechaHora = obtenerProximoDiaLaboral().withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario = Horario.reconstruir(fechaHora);
            
            // Cita cancelada con el mismo horario
            var citaCancelada = crearCitaConHorarioYTipo(fechaHora, TipoCita.ENTREGA, EstadoCita.CANCELADA);
            
            assertDoesNotThrow(() -> 
                    validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, List.of(citaCancelada)));
        }
        
        @Test
        @DisplayName("No debe haber conflicto con cita rechazada")
        void noDebeHaberConflictoConCitaRechazada() {
            var fechaHora = obtenerProximoDiaLaboral().withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario = Horario.reconstruir(fechaHora);
            
            // Cita rechazada con el mismo horario
            var citaRechazada = crearCitaConHorarioYTipo(fechaHora, TipoCita.ENTREGA, EstadoCita.RECHAZADA);
            
            assertDoesNotThrow(() -> 
                    validadorHorarios.validarDisponibilidad(horario, TipoCita.ENTREGA, List.of(citaRechazada)));
        }
    }
    
    // Métodos auxiliares
    
    private Horario crearHorarioEnDiaLaboral(int hora) {
        var diaLaboral = obtenerProximoDiaLaboral().withHour(hora).withMinute(0).withSecond(0).withNano(0);
        return Horario.reconstruir(diaLaboral);
    }
    
    private LocalDateTime obtenerProximoDiaLaboral() {
        var fecha = LocalDateTime.now().plusDays(1);
        while (fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }
    
    private LocalDateTime obtenerProximoSabado() {
        var fecha = LocalDateTime.now().plusDays(1);
        while (fecha.getDayOfWeek() != DayOfWeek.SATURDAY) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }
    
    private Cita crearCitaConHorarioYTipo(LocalDateTime fechaHora, TipoCita tipoCita, EstadoCita estado) {
        var contacto = new DatosContacto("Juan", "juan@email.com", "123");
        var proveedor = new InformacionProveedor("Proveedor", "123", "OC-1", contacto);
        var transporte = new TransporteTransportadora("Trans", "GUIA-1");
        var horario = Horario.reconstruir(fechaHora);
        
        return new Cita("test-id", tipoCita, proveedor, transporte, horario,
                estado, null, null, LocalDateTime.now(), LocalDateTime.now());
    }
}
