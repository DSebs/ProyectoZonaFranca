package com.tayronadev.api.usuario.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class AutenticacionResponse {

    private String token; // JWT
    private Long usuarioId;
    private String nombre;
    private String rol;
    private Instant expiracionToken;

}
