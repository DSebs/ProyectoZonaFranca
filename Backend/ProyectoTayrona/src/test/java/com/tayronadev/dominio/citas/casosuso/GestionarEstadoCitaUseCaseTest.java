package com.tayronadev.dominio.citas.casosuso;

import com.tayronadev.dominio.citas.excepciones.CitaNoEncontradaException;
import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.repositorios.CitaRepositorio;
import com.tayronadev.dominio.citas.servicios.GestorEstadosCita;
import com.tayronadev.dominio.notificacion.casosuso.NotificarCambioEstadoCitaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GestionarEstadoCitaUseCase - Caso de Uso")
class GestionarEstadoCitaUseCaseTest {
    
    @Mock
    private CitaRepositorio citaRepositorio;
    
    @Mock
    private GestorEstadosCita gestorEstados;
    
    @Mock
    private NotificarCambioEstadoCitaUseCase notificarCambioEstado;
    
    @InjectMocks
    private GestionarEstadoCitaUseCase gestionarEstadoUseCase;
    
    private Cita citaPendiente;
    private Cita citaConfirmada;
    
    @BeforeEach
    void setUp() {
        citaPendiente = crearCita("cita-1", EstadoCita.PENDIENTE);
        citaConfirmada = crearCita("cita-2", EstadoCita.CONFIRMADA);
    }
    
    @Nested
    @DisplayName("Confirmar cita")
    class ConfirmarCita {
        
        @Test
        @DisplayName("Debe confirmar una cita pendiente")
        void debeConfirmarCitaPendiente() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            doNothing().when(notificarCambioEstado).ejecutar(any(), anyString());
            
            // When
            Cita resultado = gestionarEstadoUseCase.confirmarCita("cita-1", "Confirmada");
            
            // Then
            assertEquals(EstadoCita.CONFIRMADA, resultado.getEstado());
            verify(citaRepositorio).guardar(any(Cita.class));
            verify(notificarCambioEstado).ejecutar(any(Cita.class), eq("Confirmada"));
        }
        
        @Test
        @DisplayName("Debe lanzar excepción si la cita no existe")
        void debeLanzarExcepcionSiCitaNoExiste() {
            // Given
            when(citaRepositorio.buscarPorId("no-existe")).thenReturn(Optional.empty());
            
            // When & Then
            assertThrows(CitaNoEncontradaException.class, 
                    () -> gestionarEstadoUseCase.confirmarCita("no-existe", "Obs"));
            
            verify(citaRepositorio, never()).guardar(any());
            verify(notificarCambioEstado, never()).ejecutar(any(), anyString());
        }
    }
    
    @Nested
    @DisplayName("Rechazar cita")
    class RechazarCita {
        
        @Test
        @DisplayName("Debe rechazar una cita pendiente con motivo")
        void debeRechazarCitaPendienteConMotivo() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            doNothing().when(notificarCambioEstado).ejecutar(any(), anyString());
            
            // When
            Cita resultado = gestionarEstadoUseCase.rechazarCita("cita-1", "Documentación incompleta");
            
            // Then
            assertEquals(EstadoCita.RECHAZADA, resultado.getEstado());
            assertEquals("Documentación incompleta", resultado.getObservaciones().orElse(""));
            verify(notificarCambioEstado).ejecutar(any(Cita.class), eq("Documentación incompleta"));
        }
        
        @Test
        @DisplayName("Debe lanzar excepción si no se proporciona motivo")
        void debeLanzarExcepcionSinMotivo() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            
            // When & Then
            assertThrows(NullPointerException.class, 
                    () -> gestionarEstadoUseCase.rechazarCita("cita-1", null));
        }
    }
    
    @Nested
    @DisplayName("Cancelar cita")
    class CancelarCita {
        
        @Test
        @DisplayName("Debe cancelar una cita pendiente")
        void debeCancelarCitaPendiente() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            doNothing().when(notificarCambioEstado).ejecutar(any(), anyString());
            
            // When
            Cita resultado = gestionarEstadoUseCase.cancelarCita("cita-1", "Proveedor solicitó cancelación");
            
            // Then
            assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
            verify(notificarCambioEstado).ejecutar(any(Cita.class), eq("Proveedor solicitó cancelación"));
        }
        
        @Test
        @DisplayName("Debe cancelar una cita confirmada")
        void debeCancelarCitaConfirmada() {
            // Given
            when(citaRepositorio.buscarPorId("cita-2")).thenReturn(Optional.of(citaConfirmada));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            doNothing().when(notificarCambioEstado).ejecutar(any(), anyString());
            
            // When
            Cita resultado = gestionarEstadoUseCase.cancelarCita("cita-2", "Emergencia");
            
            // Then
            assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        }
        
        @Test
        @DisplayName("No debe cancelar una cita rechazada")
        void noDebeCancelarCitaRechazada() {
            // Given
            var citaRechazada = crearCita("cita-3", EstadoCita.RECHAZADA);
            when(citaRepositorio.buscarPorId("cita-3")).thenReturn(Optional.of(citaRechazada));
            
            // When & Then
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> gestionarEstadoUseCase.cancelarCita("cita-3", "Motivo"));
        }
    }
    
    @Nested
    @DisplayName("Estados Post-Cita")
    class EstadosPostCita {
        
        @Test
        @DisplayName("Debe marcar como entregada una cita confirmada")
        void debeMarcarComoEntregada() {
            // Given
            when(citaRepositorio.buscarPorId("cita-2")).thenReturn(Optional.of(citaConfirmada));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            
            // When
            Cita resultado = gestionarEstadoUseCase.marcarComoEntregada("cita-2");
            
            // Then
            assertTrue(resultado.getEstadoPostCita().isPresent());
            assertEquals(EstadoPostCita.ENTREGADO, resultado.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("Debe marcar como devuelta una cita confirmada")
        void debeMarcarComoDevuelta() {
            // Given
            when(citaRepositorio.buscarPorId("cita-2")).thenReturn(Optional.of(citaConfirmada));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            
            // When
            Cita resultado = gestionarEstadoUseCase.marcarComoDevuelta("cita-2");
            
            // Then
            assertEquals(EstadoPostCita.DEVUELTO, resultado.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("Debe marcar como tardía una cita confirmada")
        void debeMarcarComoTardia() {
            // Given
            when(citaRepositorio.buscarPorId("cita-2")).thenReturn(Optional.of(citaConfirmada));
            when(citaRepositorio.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
            
            // When
            Cita resultado = gestionarEstadoUseCase.marcarComoTardia("cita-2");
            
            // Then
            assertEquals(EstadoPostCita.TARDIA, resultado.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("No debe asignar estado post-cita a cita pendiente")
        void noDebeAsignarEstadoPostACitaPendiente() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            
            // When & Then
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> gestionarEstadoUseCase.marcarComoEntregada("cita-1"));
        }
    }
    
    @Nested
    @DisplayName("Verificación de permisos")
    class VerificacionPermisos {
        
        @Test
        @DisplayName("Debe verificar si cita puede ser modificada por administrador")
        void debeVerificarSiPuedeSerModificada() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            when(gestorEstados.puedeSerModificadaPorAdministrador(citaPendiente)).thenReturn(true);
            
            // When
            boolean resultado = gestionarEstadoUseCase.puedeSerModificadaPorAdministrador("cita-1");
            
            // Then
            assertTrue(resultado);
            verify(gestorEstados).puedeSerModificadaPorAdministrador(citaPendiente);
        }
        
        @Test
        @DisplayName("Debe verificar si cita puede ser cancelada por proveedor")
        void debeVerificarSiPuedeSerCancelada() {
            // Given
            when(citaRepositorio.buscarPorId("cita-1")).thenReturn(Optional.of(citaPendiente));
            when(gestorEstados.puedeSerCanceladaPorProveedor(citaPendiente)).thenReturn(true);
            
            // When
            boolean resultado = gestionarEstadoUseCase.puedeSerCanceladaPorProveedor("cita-1");
            
            // Then
            assertTrue(resultado);
            verify(gestorEstados).puedeSerCanceladaPorProveedor(citaPendiente);
        }
    }
    
    // Método auxiliar
    private Cita crearCita(String id, EstadoCita estado) {
        var contacto = new DatosContacto("Juan", "juan@email.com", "123");
        var proveedor = new InformacionProveedor("Proveedor", "123", "OC-1", contacto);
        var transporte = new TransporteTransportadora("Trans", "GUIA-1");
        var horario = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
        
        return new Cita(id, TipoCita.ENTREGA, proveedor, transporte, horario,
                estado, null, null, LocalDateTime.now(), LocalDateTime.now());
    }
}
