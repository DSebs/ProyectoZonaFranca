package com.tayronadev.dominio.notificacion.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoNotificacion - Enum")
class TipoNotificacionTest {
    
    @Test
    @DisplayName("CITA_CONFIRMADA debe tener asunto y descripción correctos")
    void citaConfirmadaDebeTenerDatosCorrectos() {
        assertEquals("Cita Confirmada", TipoNotificacion.CITA_CONFIRMADA.getAsunto());
        assertEquals("Su cita ha sido confirmada exitosamente", TipoNotificacion.CITA_CONFIRMADA.getDescripcion());
    }
    
    @Test
    @DisplayName("CITA_RECHAZADA debe tener asunto y descripción correctos")
    void citaRechazadaDebeTenerDatosCorrectos() {
        assertEquals("Cita Rechazada", TipoNotificacion.CITA_RECHAZADA.getAsunto());
        assertEquals("Su solicitud de cita ha sido rechazada", TipoNotificacion.CITA_RECHAZADA.getDescripcion());
    }
    
    @Test
    @DisplayName("CITA_CANCELADA debe tener asunto y descripción correctos")
    void citaCanceladaDebeTenerDatosCorrectos() {
        assertEquals("Cita Cancelada", TipoNotificacion.CITA_CANCELADA.getAsunto());
        assertEquals("Su cita ha sido cancelada", TipoNotificacion.CITA_CANCELADA.getDescripcion());
    }
    
    @Test
    @DisplayName("Debe haber exactamente 3 tipos de notificación")
    void debeHaber3TiposDeNotificacion() {
        assertEquals(3, TipoNotificacion.values().length);
    }
}
