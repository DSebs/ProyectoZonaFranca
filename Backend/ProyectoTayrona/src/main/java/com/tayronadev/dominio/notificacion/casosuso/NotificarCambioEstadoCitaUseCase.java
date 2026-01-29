package com.tayronadev.dominio.notificacion.casosuso;

import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.notificacion.modelo.NotificacionCita;
import com.tayronadev.dominio.notificacion.modelo.TipoNotificacion;
import com.tayronadev.dominio.notificacion.puertos.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para notificar al proveedor cuando cambia el estado de una cita.
 * Las notificaciones se envían de forma asíncrona para no bloquear el flujo principal.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificarCambioEstadoCitaUseCase {
    
    private final NotificacionService notificacionService;
    
    /**
     * Notifica al proveedor sobre el cambio de estado de su cita.
     * Se ejecuta de forma asíncrona.
     * 
     * @param cita la cita que cambió de estado
     * @param observaciones observaciones o motivo del cambio
     */
    @Async
    public void ejecutar(Cita cita, String observaciones) {
        var tipoNotificacion = determinarTipoNotificacion(cita.getEstado());
        
        if (tipoNotificacion == null) {
            log.debug("No se requiere notificación para el estado: {}", cita.getEstado());
            return;
        }
        
        var notificacion = construirNotificacion(cita, tipoNotificacion, observaciones);
        
        try {
            log.info("Enviando notificación de {} a {} para cita {}", 
                    tipoNotificacion, 
                    notificacion.getDestinatarioEmail(),
                    cita.getId());
            
            notificacionService.enviarNotificacionCita(notificacion);
            
            log.info("Notificación enviada exitosamente para cita {}", cita.getId());
        } catch (Exception e) {
            // No propagamos la excepción para no afectar el flujo principal
            log.error("Error al enviar notificación para cita {}: {}", 
                    cita.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * Determina el tipo de notificación según el estado de la cita
     */
    private TipoNotificacion determinarTipoNotificacion(EstadoCita estado) {
        return switch (estado) {
            case CONFIRMADA -> TipoNotificacion.CITA_CONFIRMADA;
            case RECHAZADA -> TipoNotificacion.CITA_RECHAZADA;
            case CANCELADA -> TipoNotificacion.CITA_CANCELADA;
            case PENDIENTE -> null; // No se notifica estado pendiente
        };
    }
    
    /**
     * Construye el objeto de notificación con todos los datos necesarios
     */
    private NotificacionCita construirNotificacion(Cita cita, TipoNotificacion tipo, String observaciones) {
        var proveedor = cita.getProveedor();
        var responsable = proveedor.getResponsable();
        
        return NotificacionCita.builder()
                .destinatarioEmail(responsable.getEmail())
                .destinatarioNombre(responsable.getNombre())
                .nombreProveedor(proveedor.getNombreProveedor())
                .nit(proveedor.getNit())
                .citaId(cita.getId())
                .tipoCita(cita.getTipoCita().name())
                .tipoCitaDescripcion(cita.getTipoCita().getDescripcion())
                .fechaHoraCita(cita.getHorario().getFechaHora())
                .tipoNotificacion(tipo)
                .observaciones(observaciones)
                .build();
    }
}
