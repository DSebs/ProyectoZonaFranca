package com.tayronadev.api.citas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO de respuesta resumido para listados de citas
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CitaResumenResponse {
    
    String id;
    String tipoCita;
    String tipoCitaDescripcion;
    String estado;
    String estadoDescripcion;
    String estadoPostCita;
    
    // Datos resumidos del proveedor
    String nombreProveedor;
    String nit;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaHora;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaCreacion;
}
