package com.tayronadev.api.usuario.dto.request;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IniciarSesionRequest {

    @NotNull(message = "La informacion del usuario debe ser completada correctamente")
    @Valid
    private InformaciónInicioDeSesiónRequest informaciónInicioDeSesiónRequest;

}
