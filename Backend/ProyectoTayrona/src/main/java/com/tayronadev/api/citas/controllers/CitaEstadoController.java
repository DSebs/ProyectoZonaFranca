package com.tayronadev.api.citas.controllers;

import com.tayronadev.api.citas.dto.request.AsignarEstadoPostCitaRequest;
import com.tayronadev.api.citas.dto.request.CancelarCitaRequest;
import com.tayronadev.api.citas.dto.request.ConfirmarCitaRequest;
import com.tayronadev.api.citas.dto.request.RechazarCitaRequest;
import com.tayronadev.api.citas.dto.response.CitaResponse;
import com.tayronadev.api.citas.mappers.CitaDtoMapper;
import com.tayronadev.api.common.ApiResponse;
import com.tayronadev.dominio.citas.casosuso.GestionarEstadoCitaUseCase;
import com.tayronadev.dominio.citas.modelo.EstadoPostCita;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar los estados de las citas.
 * Maneja las operaciones de confirmar, rechazar, cancelar y asignar estados post-cita.
 */
@RestController
@RequestMapping("/api/citas/{id}")
@RequiredArgsConstructor
@Slf4j
public class CitaEstadoController {
    
    private final GestionarEstadoCitaUseCase gestionarEstadoUseCase;
    private final CitaDtoMapper mapper;
    
    /**
     * Confirma una cita pendiente
     */
    @PutMapping("/confirmar")
    public ResponseEntity<ApiResponse<CitaResponse>> confirmarCita(
            @PathVariable String id,
            @Valid @RequestBody(required = false) ConfirmarCitaRequest request) {
        
        log.info("Confirmando cita con ID: {}", id);
        
        String observaciones = request != null ? request.getObservaciones() : null;
        var cita = gestionarEstadoUseCase.confirmarCita(id, observaciones);
        var response = mapper.toCitaResponse(cita);
        
        log.info("Cita {} confirmada exitosamente", id);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Cita confirmada exitosamente"));
    }
    
    /**
     * Rechaza una cita pendiente
     */
    @PutMapping("/rechazar")
    public ResponseEntity<ApiResponse<CitaResponse>> rechazarCita(
            @PathVariable String id,
            @Valid @RequestBody RechazarCitaRequest request) {
        
        log.info("Rechazando cita con ID: {}", id);
        
        var cita = gestionarEstadoUseCase.rechazarCita(id, request.getMotivoRechazo());
        var response = mapper.toCitaResponse(cita);
        
        log.info("Cita {} rechazada exitosamente", id);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Cita rechazada"));
    }
    
    /**
     * Cancela una cita (pendiente o confirmada)
     */
    @PutMapping("/cancelar")
    public ResponseEntity<ApiResponse<CitaResponse>> cancelarCita(
            @PathVariable String id,
            @Valid @RequestBody CancelarCitaRequest request) {
        
        log.info("Cancelando cita con ID: {}", id);
        
        var cita = gestionarEstadoUseCase.cancelarCita(id, request.getMotivoCancelacion());
        var response = mapper.toCitaResponse(cita);
        
        log.info("Cita {} cancelada exitosamente", id);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Cita cancelada"));
    }
    
    /**
     * Asigna un estado post-cita (solo para citas confirmadas)
     */
    @PutMapping("/estado-post")
    public ResponseEntity<ApiResponse<CitaResponse>> asignarEstadoPostCita(
            @PathVariable String id,
            @Valid @RequestBody AsignarEstadoPostCitaRequest request) {
        
        log.info("Asignando estado post-cita {} a cita con ID: {}", request.getEstadoPostCita(), id);
        
        var cita = switch (request.getEstadoPostCita()) {
            case ENTREGADO -> gestionarEstadoUseCase.marcarComoEntregada(id);
            case DEVUELTO -> gestionarEstadoUseCase.marcarComoDevuelta(id);
            case TARDIA -> gestionarEstadoUseCase.marcarComoTardia(id);
        };
        
        var response = mapper.toCitaResponse(cita);
        
        log.info("Estado post-cita asignado exitosamente a cita {}", id);
        
        return ResponseEntity.ok(ApiResponse.success(response, 
                "Estado post-cita '" + request.getEstadoPostCita().getDescripcion() + "' asignado"));
    }
    
    /**
     * Verifica si una cita puede ser modificada por un administrador
     */
    @GetMapping("/puede-modificar")
    public ResponseEntity<ApiResponse<Boolean>> puedeSerModificada(@PathVariable String id) {
        log.debug("Verificando si la cita {} puede ser modificada", id);
        
        var puedeModificar = gestionarEstadoUseCase.puedeSerModificadaPorAdministrador(id);
        
        return ResponseEntity.ok(ApiResponse.success(puedeModificar));
    }
    
    /**
     * Verifica si una cita puede ser cancelada por el proveedor
     */
    @GetMapping("/puede-cancelar")
    public ResponseEntity<ApiResponse<Boolean>> puedeSerCancelada(@PathVariable String id) {
        log.debug("Verificando si la cita {} puede ser cancelada", id);
        
        var puedeCancelar = gestionarEstadoUseCase.puedeSerCanceladaPorProveedor(id);
        
        return ResponseEntity.ok(ApiResponse.success(puedeCancelar));
    }
}
