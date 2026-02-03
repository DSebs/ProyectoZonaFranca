package com.tayronadev.api.usuario.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {

    private String token;
    private String refreshToken;
    private String usuarioId;
    private String rol;


}
