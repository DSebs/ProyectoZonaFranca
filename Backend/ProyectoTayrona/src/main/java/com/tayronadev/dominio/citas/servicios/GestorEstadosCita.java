package com.tayronadev.dominio.citas.servicios;

import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import com.tayronadev.dominio.citas.modelo.Cita;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio para gestionar las transiciones de estado de las citas
 */
@Service
@Slf4j
public class GestorEstadosCita {
    
    /**
     * Valida si una transición de estado es válida
     */
    public boolean esTransicionValida(EstadoCita estadoActual, EstadoCita nuevoEstado) {
        return switch (estadoActual) {
            case PENDIENTE -> nuevoEstado == EstadoCita.CONFIRMADA || 
                            nuevoEstado == EstadoCita.RECHAZADA || 
                            nuevoEstado == EstadoCita.CANCELADA;
            
            case CONFIRMADA -> nuevoEstado == EstadoCita.CANCELADA;
            
            case RECHAZADA, CANCELADA -> false; // Estados finales
        };
    }
    
    /**
     * Valida una transición de estado y lanza excepción si no es válida
     */
    public void validarTransicion(EstadoCita estadoActual, EstadoCita nuevoEstado, String operacion) {
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new EstadoCitaInvalidoException(estadoActual, operacion);
        }
    }
    
    /**
     * Determina si una cita puede ser modificada por un administrador
     */
    public boolean puedeSerModificadaPorAdministrador(Cita cita) {
        // Los administradores pueden modificar citas que no estén en estados finales
        return !cita.getEstado().esFinal();
    }
    
    /**
     * Determina si una cita puede ser cancelada por el proveedor
     */
    public boolean puedeSerCanceladaPorProveedor(Cita cita) {
        // Los proveedores solo pueden cancelar citas pendientes o confirmadas
        return cita.getEstado() == EstadoCita.PENDIENTE || 
               cita.getEstado() == EstadoCita.CONFIRMADA;
    }
}