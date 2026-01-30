package com.tayronadev.api.usuario.dto.response;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConfirmarResponse {

    String mensaje;

}
