package com.tayronadev.api.citas.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * DTO de respuesta para los datos de contacto
 */
@Value
@Builder
public class DatosContactoResponse {
    String nombre;
    String email;
    String telefono;
}
