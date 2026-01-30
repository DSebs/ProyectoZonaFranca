package com.tayronadev.api.usuario.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioResponse {

    String id;
    String nombre;
    String correo;
    TipoUsuario tipoUsuario;
    boolean cuentaActiva;
}
