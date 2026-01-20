package com.tayronadev.dominio.notificacion.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotificacionCita - Value Object")
class NotificacionCitaTest {
    
    @Nested
    @DisplayName("Creación de notificación")
    class CreacionNotificacion {
        
        @Test
        @DisplayName("Debe crear notificación con todos los datos")
        void debeCrearNotificacionConTodosLosDatos() {
            var notificacion = NotificacionCita.builder()
                    .destinatarioEmail("juan@email.com")
                    .destinatarioNombre("Juan Pérez")
                    .nombreProveedor("Proveedor ABC")
                    .nit("900123456-1")
                    .citaId("cita-123")
                    .tipoCita("ENTREGA")
                    .tipoCitaDescripcion("Entrega de mercancía")
                    .fechaHoraCita(LocalDateTime.of(2025, 1, 20, 10, 0))
                    .tipoNotificacion(TipoNotificacion.CITA_CONFIRMADA)
                    .observaciones("Sin observaciones")
                    .build();
            
            assertEquals("juan@email.com", notificacion.getDestinatarioEmail());
            assertEquals("Juan Pérez", notificacion.getDestinatarioNombre());
            assertEquals("Proveedor ABC", notificacion.getNombreProveedor());
            assertEquals("cita-123", notificacion.getCitaId());
            assertEquals(TipoNotificacion.CITA_CONFIRMADA, notificacion.getTipoNotificacion());
        }
    }
    
    @Nested
    @DisplayName("Asunto del correo")
    class AsuntoCorreo {
        
        @Test
        @DisplayName("Debe generar asunto para confirmación")
        void debeGenerarAsuntoParaConfirmacion() {
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA, "Entrega");
            
            String asunto = notificacion.getAsuntoCorreo();
            
            assertTrue(asunto.contains("Cita Confirmada"));
            assertTrue(asunto.contains("Entrega"));
            assertTrue(asunto.contains("[Zona Franca]"));
        }
        
        @Test
        @DisplayName("Debe generar asunto para rechazo")
        void debeGenerarAsuntoParaRechazo() {
            var notificacion = crearNotificacion(TipoNotificacion.CITA_RECHAZADA, "Importación");
            
            String asunto = notificacion.getAsuntoCorreo();
            
            assertTrue(asunto.contains("Cita Rechazada"));
            assertTrue(asunto.contains("Importación"));
        }
        
        @Test
        @DisplayName("Debe generar asunto para cancelación")
        void debeGenerarAsuntoParaCancelacion() {
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CANCELADA, "Recojo");
            
            String asunto = notificacion.getAsuntoCorreo();
            
            assertTrue(asunto.contains("Cita Cancelada"));
            assertTrue(asunto.contains("Recojo"));
        }
    }
    
    @Nested
    @DisplayName("Verificación de tipo de notificación")
    class VerificacionTipoNotificacion {
        
        @Test
        @DisplayName("esConfirmacion debe ser true solo para CITA_CONFIRMADA")
        void esConfirmacionDebeSerTrueSoloParaConfirmada() {
            var confirmada = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA, "Test");
            var rechazada = crearNotificacion(TipoNotificacion.CITA_RECHAZADA, "Test");
            var cancelada = crearNotificacion(TipoNotificacion.CITA_CANCELADA, "Test");
            
            assertTrue(confirmada.esConfirmacion());
            assertFalse(rechazada.esConfirmacion());
            assertFalse(cancelada.esConfirmacion());
        }
        
        @Test
        @DisplayName("esRechazo debe ser true solo para CITA_RECHAZADA")
        void esRechazoDebeSerTrueSoloParaRechazada() {
            var confirmada = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA, "Test");
            var rechazada = crearNotificacion(TipoNotificacion.CITA_RECHAZADA, "Test");
            var cancelada = crearNotificacion(TipoNotificacion.CITA_CANCELADA, "Test");
            
            assertFalse(confirmada.esRechazo());
            assertTrue(rechazada.esRechazo());
            assertFalse(cancelada.esRechazo());
        }
        
        @Test
        @DisplayName("esCancelacion debe ser true solo para CITA_CANCELADA")
        void esCancelacionDebeSerTrueSoloParaCancelada() {
            var confirmada = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA, "Test");
            var rechazada = crearNotificacion(TipoNotificacion.CITA_RECHAZADA, "Test");
            var cancelada = crearNotificacion(TipoNotificacion.CITA_CANCELADA, "Test");
            
            assertFalse(confirmada.esCancelacion());
            assertFalse(rechazada.esCancelacion());
            assertTrue(cancelada.esCancelacion());
        }
    }
    
    // Método auxiliar
    private NotificacionCita crearNotificacion(TipoNotificacion tipo, String tipoCitaDesc) {
        return NotificacionCita.builder()
                .destinatarioEmail("test@email.com")
                .destinatarioNombre("Test")
                .nombreProveedor("Proveedor Test")
                .nit("123456")
                .citaId("cita-test")
                .tipoCita("TEST")
                .tipoCitaDescripcion(tipoCitaDesc)
                .fechaHoraCita(LocalDateTime.now())
                .tipoNotificacion(tipo)
                .observaciones(null)
                .build();
    }
}
