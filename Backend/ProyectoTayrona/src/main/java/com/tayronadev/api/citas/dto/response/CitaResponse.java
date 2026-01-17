package com.tayronadev.api.citas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO de respuesta completo para una cita
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CitaResponse {
    
    String id;
    String tipoCita;
    String tipoCitaDescripcion;
    String estado;
    String estadoDescripcion;
    String estadoPostCita;
    String estadoPostCitaDescripcion;
    
    InformacionProveedorResponse proveedor;
    TransporteResponse transporte;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaHora;
    
    String observaciones;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaUltimaModificacion;
    
    // Campos de utilidad para el frontend
    Boolean puedeSerModificada;
    Boolean puedeSerCancelada;
}
