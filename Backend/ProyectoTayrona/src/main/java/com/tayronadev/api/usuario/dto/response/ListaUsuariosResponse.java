package com.tayronadev.api.usuario.dto.response;

import lombok.Builder;
import lombok.Value;
import java.util.List;


@Value
@Builder
public class ListaUsuariosResponse {

    private List<UsuarioResponse> usuarios;

}
