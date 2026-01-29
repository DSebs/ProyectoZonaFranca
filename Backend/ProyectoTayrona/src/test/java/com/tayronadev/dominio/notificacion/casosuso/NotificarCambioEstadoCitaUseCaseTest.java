package com.tayronadev.dominio.notificacion.casosuso;

import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.notificacion.modelo.NotificacionCita;
import com.tayronadev.dominio.notificacion.modelo.TipoNotificacion;
import com.tayronadev.dominio.notificacion.puertos.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificarCambioEstadoCitaUseCase - Caso de Uso")
class NotificarCambioEstadoCitaUseCaseTest {
    
    @Mock
    private NotificacionService notificacionService;
    
    @InjectMocks
    private NotificarCambioEstadoCitaUseCase notificarCambioEstadoUseCase;
    
    private Cita citaConfirmada;
    private Cita citaRechazada;
    private Cita citaCancelada;
    private Cita citaPendiente;
    
    @BeforeEach
    void setUp() {
        citaConfirmada = crearCita("cita-1", EstadoCita.CONFIRMADA);
        citaRechazada = crearCita("cita-2", EstadoCita.RECHAZADA);
        citaCancelada = crearCita("cita-3", EstadoCita.CANCELADA);
        citaPendiente = crearCita("cita-4", EstadoCita.PENDIENTE);
    }
    
    @Nested
    @DisplayName("Envío de notificaciones según estado")
    class EnvioSegunEstado {
        
        @Test
        @DisplayName("Debe enviar notificación para cita CONFIRMADA")
        void debeEnviarNotificacionParaConfirmada() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaConfirmada, "Cita confirmada exitosamente");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals(TipoNotificacion.CITA_CONFIRMADA, notificacion.getTipoNotificacion());
            assertTrue(notificacion.esConfirmacion());
        }
        
        @Test
        @DisplayName("Debe enviar notificación para cita RECHAZADA")
        void debeEnviarNotificacionParaRechazada() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaRechazada, "Documentación incompleta");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals(TipoNotificacion.CITA_RECHAZADA, notificacion.getTipoNotificacion());
            assertTrue(notificacion.esRechazo());
            assertEquals("Documentación incompleta", notificacion.getObservaciones());
        }
        
        @Test
        @DisplayName("Debe enviar notificación para cita CANCELADA")
        void debeEnviarNotificacionParaCancelada() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaCancelada, "Cancelada por el proveedor");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals(TipoNotificacion.CITA_CANCELADA, notificacion.getTipoNotificacion());
            assertTrue(notificacion.esCancelacion());
        }
        
        @Test
        @DisplayName("No debe enviar notificación para cita PENDIENTE")
        void noDebeEnviarNotificacionParaPendiente() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaPendiente, "Observación");
            
            // Then
            verify(notificacionService, never()).enviarNotificacionCita(any());
        }
    }
    
    @Nested
    @DisplayName("Datos de la notificación")
    class DatosNotificacion {
        
        @Test
        @DisplayName("Debe incluir datos del destinatario correctos")
        void debeIncluirDatosDestinatarioCorrectos() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaConfirmada, "Obs");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals("juan@email.com", notificacion.getDestinatarioEmail());
            assertEquals("Juan Pérez", notificacion.getDestinatarioNombre());
        }
        
        @Test
        @DisplayName("Debe incluir datos del proveedor correctos")
        void debeIncluirDatosProveedorCorrectos() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaConfirmada, "Obs");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals("Proveedor ABC", notificacion.getNombreProveedor());
            assertEquals("900123456-1", notificacion.getNit());
        }
        
        @Test
        @DisplayName("Debe incluir datos de la cita correctos")
        void debeIncluirDatosCitaCorrectos() {
            // When
            notificarCambioEstadoUseCase.ejecutar(citaConfirmada, "Obs");
            
            // Then
            ArgumentCaptor<NotificacionCita> captor = ArgumentCaptor.forClass(NotificacionCita.class);
            verify(notificacionService).enviarNotificacionCita(captor.capture());
            
            NotificacionCita notificacion = captor.getValue();
            assertEquals("cita-1", notificacion.getCitaId());
            assertEquals("ENTREGA", notificacion.getTipoCita());
            assertNotNull(notificacion.getFechaHoraCita());
        }
    }
    
    @Nested
    @DisplayName("Manejo de errores")
    class ManejoErrores {
        
        @Test
        @DisplayName("No debe propagar excepción si falla el envío")
        void noDebePropararExcepcionSiFallaEnvio() {
            // Given
            doThrow(new RuntimeException("Error de envío"))
                    .when(notificacionService).enviarNotificacionCita(any());
            
            // When & Then - No debe lanzar excepción
            assertDoesNotThrow(() -> 
                    notificarCambioEstadoUseCase.ejecutar(citaConfirmada, "Obs"));
        }
    }
    
    // Método auxiliar
    private Cita crearCita(String id, EstadoCita estado) {
        var contacto = new DatosContacto("Juan Pérez", "juan@email.com", "3001234567");
        var proveedor = new InformacionProveedor("Proveedor ABC", "900123456-1", "OC-001", contacto);
        var transporte = new TransporteTransportadora("Servientrega", "GUIA-123");
        var horario = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
        
        return new Cita(id, TipoCita.ENTREGA, proveedor, transporte, horario,
                estado, null, null, LocalDateTime.now(), LocalDateTime.now());
    }
}
