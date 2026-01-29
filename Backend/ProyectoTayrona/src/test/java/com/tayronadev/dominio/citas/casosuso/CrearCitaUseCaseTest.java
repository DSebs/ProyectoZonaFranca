package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.HorarioNoDisponibleException;
import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.ValidadorHorarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearCitaUseCase - Caso de Uso")
class CrearCitaUseCaseTest {
    
    @Mock
    private CitaRepositorio citaRepositorio;
    
    @Mock
    private ValidadorHorarios validadorHorarios;
    
    @InjectMocks
    private CrearCitaUseCase crearCitaUseCase;
    
    private InformacionProveedor proveedor;
    private OpcionTransporte transporte;
    private Horario horario;
    
    @BeforeEach
    void setUp() {
        var contacto = new DatosContacto("Juan Pérez", "juan@email.com", "3001234567");
        proveedor = new InformacionProveedor("Proveedor ABC", "900123456-1", "OC-001", contacto);
        transporte = new TransporteTransportadora("Servientrega", "GUIA-123");
        horario = new Horario(obtenerProximoDiaLaboralA(10));
    }
    
    @Nested
    @DisplayName("Crear cita exitosamente")
    class CrearCitaExitosamente {
        
        @Test
        @DisplayName("Debe crear una cita cuando el horario está disponible")
        void debeCrearCitaCuandoHorarioDisponible() {
            // Given
            when(citaRepositorio.buscarActivasPorTipo(TipoCita.ENTREGA)).thenReturn(Collections.emptyList());
            doNothing().when(validadorHorarios).validarDisponibilidad(any(), any(), any());
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
            
            // When
            Cita resultado = crearCitaUseCase.ejecutar(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            // Then
            assertNotNull(resultado);
            assertEquals(TipoCita.ENTREGA, resultado.getTipoCita());
            assertEquals(EstadoCita.PENDIENTE, resultado.getEstado());
            assertEquals(proveedor, resultado.getProveedor());
            
            verify(citaRepositorio).buscarActivasPorTipo(TipoCita.ENTREGA);
            verify(validadorHorarios).validarDisponibilidad(horario, TipoCita.ENTREGA, Collections.emptyList());
            verify(citaRepositorio).guardar(any(Cita.class));
        }
        
        @Test
        @DisplayName("Debe validar disponibilidad antes de crear")
        void debeValidarDisponibilidadAntesDeCrear() {
            // Given
            var citasExistentes = List.of(crearCitaMock(TipoCita.ENTREGA, EstadoCita.CONFIRMADA));
            when(citaRepositorio.buscarActivasPorTipo(TipoCita.ENTREGA)).thenReturn(citasExistentes);
            doNothing().when(validadorHorarios).validarDisponibilidad(any(), any(), any());
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
            
            // When
            crearCitaUseCase.ejecutar(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            // Then
            verify(validadorHorarios).validarDisponibilidad(horario, TipoCita.ENTREGA, citasExistentes);
        }
    }
    
    @Nested
    @DisplayName("Fallo al crear cita")
    class FalloAlCrearCita {
        
        @Test
        @DisplayName("Debe fallar cuando el horario no está disponible")
        void debeFallarCuandoHorarioNoDisponible() {
            // Given
            when(citaRepositorio.buscarActivasPorTipo(TipoCita.ENTREGA)).thenReturn(Collections.emptyList());
            doThrow(new HorarioNoDisponibleException(horario, TipoCita.ENTREGA))
                    .when(validadorHorarios).validarDisponibilidad(any(), any(), any());
            
            // When & Then
            assertThrows(HorarioNoDisponibleException.class, 
                    () -> crearCitaUseCase.ejecutar(TipoCita.ENTREGA, proveedor, transporte, horario));
            
            verify(citaRepositorio, never()).guardar(any());
        }
    }
    
    @Nested
    @DisplayName("Validar disponibilidad de horario")
    class ValidarDisponibilidadHorario {
        
        @Test
        @DisplayName("Debe retornar true cuando el horario está disponible")
        void debeRetornarTrueCuandoHorarioDisponible() {
            // Given
            when(citaRepositorio.buscarActivasPorTipo(TipoCita.ENTREGA)).thenReturn(Collections.emptyList());
            doNothing().when(validadorHorarios).validarDisponibilidad(any(), any(), any());
            
            // When
            boolean resultado = crearCitaUseCase.validarDisponibilidadHorario(TipoCita.ENTREGA, horario);
            
            // Then
            assertTrue(resultado);
        }
        
        @Test
        @DisplayName("Debe retornar false cuando el horario no está disponible")
        void debeRetornarFalseCuandoHorarioNoDisponible() {
            // Given
            when(citaRepositorio.buscarActivasPorTipo(TipoCita.ENTREGA)).thenReturn(Collections.emptyList());
            doThrow(new HorarioNoDisponibleException(horario, TipoCita.ENTREGA))
                    .when(validadorHorarios).validarDisponibilidad(any(), any(), any());
            
            // When
            boolean resultado = crearCitaUseCase.validarDisponibilidadHorario(TipoCita.ENTREGA, horario);
            
            // Then
            assertFalse(resultado);
        }
    }
    
    // Métodos auxiliares
    
    private LocalDateTime obtenerProximoDiaLaboralA(int hora) {
        var fecha = LocalDateTime.now().plusDays(1).withHour(hora).withMinute(0).withSecond(0).withNano(0);
        while (fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }
    
    private Cita crearCitaMock(TipoCita tipo, EstadoCita estado) {
        var contacto = new DatosContacto("Test", "test@email.com", "123");
        var prov = new InformacionProveedor("Test", "123", "OC-1", contacto);
        var trans = new TransporteTransportadora("Trans", "GUIA");
        var hor = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
        
        return new Cita("mock-id", tipo, prov, trans, hor, estado, null, null, 
                LocalDateTime.now(), LocalDateTime.now());
    }
}
