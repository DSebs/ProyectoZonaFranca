package com.tayronadev.api.usuario.dto.request;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioRequest {

    @NotNull(message = "La informacion del usuario debe ser completada correctamente")
    @Valid
    private InformacionCrearUsuarioRequest informacionUsuarioRequest;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuario tipoUsuario;
}
