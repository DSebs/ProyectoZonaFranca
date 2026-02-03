package com.tayronadev.api.usuario.dto.response;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UsuarioEliminadoResponse {

    String correo;
    TipoUsuario tipoUsuario;

}
