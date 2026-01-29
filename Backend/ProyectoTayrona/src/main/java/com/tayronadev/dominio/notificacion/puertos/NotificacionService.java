package com.tayronadev.dominio.notificacion.puertos;

import com.tayronadev.dominio.notificacion.modelo.NotificacionCita;

/**
 * Puerto (interfaz) para el servicio de notificaciones.
 * Define el contrato para enviar notificaciones sin acoplar a una implementación específica.
 */
public interface NotificacionService {
    
    /**
     * Envía una notificación de cita al proveedor
     * 
     * @param notificacion datos de la notificación a enviar
     */
    void enviarNotificacionCita(NotificacionCita notificacion);
    
    /**
     * Verifica si el servicio de notificaciones está disponible
     * 
     * @return true si el servicio está operativo
     */
    boolean estaDisponible();
}
