package com.tayronadev.dominio.notificacion.modelo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Value Object que representa una notificación de cita a enviar.
 * Contiene toda la información necesaria para generar el correo.
 */
@Value
@Builder
public class NotificacionCita {
    
    // Datos del destinatario
    String destinatarioEmail;
    String destinatarioNombre;
    
    // Datos del proveedor
    String nombreProveedor;
    String nit;
    
    // Datos de la cita
    String citaId;
    String tipoCita;
    String tipoCitaDescripcion;
    LocalDateTime fechaHoraCita;
    
    // Tipo de notificación y contenido
    TipoNotificacion tipoNotificacion;
    String observaciones;
    
    /**
     * Obtiene el asunto del correo
     */
    public String getAsuntoCorreo() {
        return String.format("[Zona Franca] %s - %s", 
                tipoNotificacion.getAsunto(), 
                tipoCitaDescripcion);
    }
    
    /**
     * Verifica si la notificación es de confirmación
     */
    public boolean esConfirmacion() {
        return tipoNotificacion == TipoNotificacion.CITA_CONFIRMADA;
    }
    
    /**
     * Verifica si la notificación es de rechazo
     */
    public boolean esRechazo() {
        return tipoNotificacion == TipoNotificacion.CITA_RECHAZADA;
    }
    
    /**
     * Verifica si la notificación es de cancelación
     */
    public boolean esCancelacion() {
        return tipoNotificacion == TipoNotificacion.CITA_CANCELADA;
    }
}
