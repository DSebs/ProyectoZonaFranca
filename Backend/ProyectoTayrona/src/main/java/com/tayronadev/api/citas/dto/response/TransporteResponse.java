package com.tayronadev.api.citas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

/**
 * DTO de respuesta para la información de transporte
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransporteResponse {
    String tipoTransporte;
    
    // Campos para TRANSPORTADORA
    String nombreTransportadora;
    String numeroGuia;
    
    // Campos para PARTICULAR
    String conductorNombre;
    String conductorCedula;
    String placaVehiculo;
    
    // Auxiliar (opcional)
    String auxiliarNombre;
    String auxiliarCedula;
}
