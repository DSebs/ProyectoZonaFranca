package com.tayronadev.api.citas.controllers;

import com.tayronadev.api.citas.dto.request.CrearCitaRequest;
import com.tayronadev.api.citas.dto.response.CitaResumenResponse;
import com.tayronadev.api.citas.dto.response.CitaResponse;
import com.tayronadev.api.citas.mappers.CitaDtoMapper;
import com.tayronadev.api.common.ApiResponse;
import com.tayronadev.dominio.citas.casosuso.ConsultarCitasUseCase;
import com.tayronadev.dominio.citas.casosuso.CrearCitaUseCase;
import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operaciones CRUD de citas.
 * Maneja la creación y consulta de citas.
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Slf4j
public class CitaController {
    
    private final CrearCitaUseCase crearCitaUseCase;
    private final ConsultarCitasUseCase consultarCitasUseCase;
    private final CitaDtoMapper mapper;
    
    /**
     * Crea una nueva cita.
     * Este endpoint es público para que los proveedores puedan solicitar citas.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CitaResponse>> crearCita(
            @Valid @RequestBody CrearCitaRequest request) {
        
        log.info("Recibida solicitud de creación de cita para proveedor: {}", 
                request.getProveedor().getNombreProveedor());
        
        // Validar que el transporte tenga los campos requeridos según su tipo
        if (!request.getTransporte().esValido()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Los datos de transporte son incompletos para el tipo seleccionado"));
        }
        
        var proveedor = mapper.toInformacionProveedor(request);
        var transporte = mapper.toOpcionTransporte(request.getTransporte());
        var horario = mapper.toHorario(request);
        
        var citaCreada = crearCitaUseCase.ejecutar(
                request.getTipoCita(),
                proveedor,
                transporte,
                horario
        );
        
        var response = mapper.toCitaResponse(citaCreada);
        
        log.info("Cita creada exitosamente con ID: {}", citaCreada.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Cita creada exitosamente"));
    }
    
    /**
     * Obtiene una cita por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> obtenerCitaPorId(@PathVariable String id) {
        log.debug("Consultando cita con ID: {}", id);
        
        var cita = consultarCitasUseCase.buscarPorId(id);
        var response = mapper.toCitaResponse(cita);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista todas las citas pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPendientes() {
        log.debug("Consultando citas pendientes");
        
        var citas = consultarCitasUseCase.obtenerCitasPendientes();
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista todas las citas confirmadas
     */
    @GetMapping("/confirmadas")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasConfirmadas() {
        log.debug("Consultando citas confirmadas");
        
        var citas = consultarCitasUseCase.obtenerCitasConfirmadas();
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorEstado(
            @PathVariable EstadoCita estado) {
        log.debug("Consultando citas por estado: {}", estado);
        
        var citas = consultarCitasUseCase.buscarPorEstado(estado);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorTipo(
            @PathVariable TipoCita tipo) {
        log.debug("Consultando citas por tipo: {}", tipo);
        
        var citas = consultarCitasUseCase.obtenerCitasActivasPorTipo(tipo);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas por tipo y estado
     */
    @GetMapping("/tipo/{tipo}/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorTipoYEstado(
            @PathVariable TipoCita tipo,
            @PathVariable EstadoCita estado) {
        log.debug("Consultando citas por tipo: {} y estado: {}", tipo, estado);
        
        var citas = consultarCitasUseCase.buscarPorTipoYEstado(tipo, estado);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas de un proveedor por NIT
     */
    @GetMapping("/proveedor/{nit}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorProveedor(
            @PathVariable String nit) {
        log.debug("Consultando citas del proveedor con NIT: {}", nit);
        
        var citas = consultarCitasUseCase.buscarPorProveedor(nit);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas activas de un proveedor por NIT
     */
    @GetMapping("/proveedor/{nit}/activas")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasActivasPorProveedor(
            @PathVariable String nit) {
        log.debug("Consultando citas activas del proveedor con NIT: {}", nit);
        
        var citas = consultarCitasUseCase.buscarCitasActivasProveedor(nit);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas por fecha
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.debug("Consultando citas para la fecha: {}", fecha);
        
        var citas = consultarCitasUseCase.buscarPorFecha(fecha);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Lista citas por tipo en una fecha específica
     */
    @GetMapping("/tipo/{tipo}/fecha/{fecha}")
    public ResponseEntity<ApiResponse<List<CitaResumenResponse>>> obtenerCitasPorTipoYFecha(
            @PathVariable TipoCita tipo,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.debug("Consultando citas de tipo: {} para la fecha: {}", tipo, fecha);
        
        var citas = consultarCitasUseCase.buscarPorTipoYFecha(tipo, fecha);
        var response = mapper.toCitaResumenResponseList(citas);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Verifica si existe una cita con el ID especificado
     */
    @GetMapping("/{id}/existe")
    public ResponseEntity<ApiResponse<Boolean>> existeCita(@PathVariable String id) {
        log.debug("Verificando existencia de cita con ID: {}", id);
        
        var existe = consultarCitasUseCase.existeCita(id);
        
        return ResponseEntity.ok(ApiResponse.success(existe));
    }
    
    /**
     * Obtiene el conteo de citas por estado
     */
    @GetMapping("/conteo/estado/{estado}")
    public ResponseEntity<ApiResponse<Long>> contarCitasPorEstado(@PathVariable EstadoCita estado) {
        log.debug("Contando citas por estado: {}", estado);
        
        var conteo = consultarCitasUseCase.contarPorEstado(estado);
        
        return ResponseEntity.ok(ApiResponse.success(conteo));
    }
}
