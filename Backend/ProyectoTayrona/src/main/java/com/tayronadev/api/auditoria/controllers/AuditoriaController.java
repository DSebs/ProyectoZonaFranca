package com.tayronadev.api.auditoria.controllers;

import com.tayronadev.api.auditoria.dto.response.RegistroCambioEstadoResponse;
import com.tayronadev.api.auditoria.dto.response.ResumenAuditoriaResponse;
import com.tayronadev.api.auditoria.mappers.AuditoriaDtoMapper;
import com.tayronadev.api.common.ApiResponse;
import com.tayronadev.dominio.auditoria.casosuso.ConsultarAuditoriaUseCase;
import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para consultar registros de auditoría.
 * Expone endpoints para consultar el historial de cambios de estado en citas.
 */
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@Slf4j
public class AuditoriaController {
    
    private final ConsultarAuditoriaUseCase consultarAuditoriaUseCase;
    private final AuditoriaDtoMapper mapper;
    
    /**
     * Obtiene un registro de auditoría por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RegistroCambioEstadoResponse>> obtenerPorId(
            @PathVariable String id) {
        log.info("GET /api/auditoria/{} - Obteniendo registro", id);
        
        var registro = consultarAuditoriaUseCase.obtenerPorId(id);
        var response = mapper.toResponse(registro);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Registro encontrado"));
    }
    
    /**
     * Obtiene todos los registros de auditoría de un usuario específico.
     * Este es el filtro principal según el requerimiento.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<RegistroCambioEstadoResponse>>> obtenerPorUsuario(
            @PathVariable String usuarioId) {
        log.info("GET /api/auditoria/usuario/{} - Obteniendo registros por usuario", usuarioId);
        
        var registros = consultarAuditoriaUseCase.obtenerPorUsuarioId(usuarioId);
        var response = mapper.toResponseList(registros);
        
        return ResponseEntity.ok(ApiResponse.success(
            response,
            String.format("Se encontraron %d registros para el usuario", response.size())
        ));
    }
    
    /**
     * Obtiene resumen de auditoría de un usuario
     */
    @GetMapping("/usuario/{usuarioId}/resumen")
    public ResponseEntity<ApiResponse<ResumenAuditoriaResponse>> obtenerResumenPorUsuario(
            @PathVariable String usuarioId) {
        log.info("GET /api/auditoria/usuario/{}/resumen - Obteniendo resumen", usuarioId);
        
        var registros = consultarAuditoriaUseCase.obtenerPorUsuarioId(usuarioId);
        var totalCambios = consultarAuditoriaUseCase.contarPorUsuarioId(usuarioId);
        
        // Obtener nombre del usuario del primer registro (si existe)
        String usuarioNombre = registros.isEmpty() ? "Desconocido" : registros.get(0).getUsuarioNombre();
        
        var resumen = ResumenAuditoriaResponse.builder()
            .usuarioId(usuarioId)
            .usuarioNombre(usuarioNombre)
            .totalCambiosRealizados(totalCambios)
            .ultimosCambios(mapper.toResponseList(registros.stream().limit(10).toList()))
            .build();
        
        return ResponseEntity.ok(ApiResponse.success(resumen, "Resumen de auditoría"));
    }
    
    /**
     * Obtiene todos los registros de auditoría de una cita específica
     */
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<ApiResponse<List<RegistroCambioEstadoResponse>>> obtenerPorCita(
            @PathVariable String citaId) {
        log.info("GET /api/auditoria/cita/{} - Obteniendo registros por cita", citaId);
        
        var registros = consultarAuditoriaUseCase.obtenerPorCitaId(citaId);
        var response = mapper.toResponseList(registros);
        
        return ResponseEntity.ok(ApiResponse.success(
            response,
            String.format("Se encontraron %d cambios para la cita", response.size())
        ));
    }
    
    /**
     * Obtiene registros de auditoría por tipo de cambio
     */
    @GetMapping("/tipo/{tipoCambio}")
    public ResponseEntity<ApiResponse<List<RegistroCambioEstadoResponse>>> obtenerPorTipoCambio(
            @PathVariable TipoCambio tipoCambio) {
        log.info("GET /api/auditoria/tipo/{} - Obteniendo registros por tipo", tipoCambio);
        
        var registros = consultarAuditoriaUseCase.obtenerPorTipoCambio(tipoCambio);
        var response = mapper.toResponseList(registros);
        
        return ResponseEntity.ok(ApiResponse.success(
            response,
            String.format("Se encontraron %d registros de tipo %s", response.size(), tipoCambio)
        ));
    }
    
    /**
     * Obtiene registros de auditoría en un rango de fechas
     */
    @GetMapping("/rango")
    public ResponseEntity<ApiResponse<List<RegistroCambioEstadoResponse>>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        log.info("GET /api/auditoria/rango?desde={}&hasta={}", desde, hasta);
        
        var registros = consultarAuditoriaUseCase.obtenerPorRangoFechas(desde, hasta);
        var response = mapper.toResponseList(registros);
        
        return ResponseEntity.ok(ApiResponse.success(
            response,
            String.format("Se encontraron %d registros en el rango", response.size())
        ));
    }
    
    /**
     * Obtiene los últimos N registros de auditoría
     */
    @GetMapping("/ultimos")
    public ResponseEntity<ApiResponse<List<RegistroCambioEstadoResponse>>> obtenerUltimos(
            @RequestParam(defaultValue = "20") int limite) {
        log.info("GET /api/auditoria/ultimos?limite={}", limite);
        
        var registros = consultarAuditoriaUseCase.obtenerUltimosRegistros(Math.min(limite, 100));
        var response = mapper.toResponseList(registros);
        
        return ResponseEntity.ok(ApiResponse.success(
            response,
            String.format("Últimos %d registros de auditoría", response.size())
        ));
    }
    
    /**
     * Obtiene el conteo de cambios realizados por un usuario
     */
    @GetMapping("/usuario/{usuarioId}/conteo")
    public ResponseEntity<ApiResponse<Long>> contarPorUsuario(@PathVariable String usuarioId) {
        log.info("GET /api/auditoria/usuario/{}/conteo", usuarioId);
        
        long conteo = consultarAuditoriaUseCase.contarPorUsuarioId(usuarioId);
        
        return ResponseEntity.ok(ApiResponse.success(
            conteo,
            String.format("El usuario ha realizado %d cambios", conteo)
        ));
    }
    
    /**
     * Obtiene el conteo de cambios realizados en una cita
     */
    @GetMapping("/cita/{citaId}/conteo")
    public ResponseEntity<ApiResponse<Long>> contarPorCita(@PathVariable String citaId) {
        log.info("GET /api/auditoria/cita/{}/conteo", citaId);
        
        long conteo = consultarAuditoriaUseCase.contarPorCitaId(citaId);
        
        return ResponseEntity.ok(ApiResponse.success(
            conteo,
            String.format("La cita tiene %d cambios registrados", conteo)
        ));
    }
}
