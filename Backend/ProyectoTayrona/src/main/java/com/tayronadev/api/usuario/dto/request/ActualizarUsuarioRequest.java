package com.tayronadev.api.usuario.dto.request;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    private String correo;

    private String contraseña;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$",
            message = "La contraseña debe contener al menos una mayúscula y un número"
    )
    private String nuevaContraseña;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuario tipoUsuario;

    private Boolean cuentaActiva;
}
