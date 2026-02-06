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
public class InformaciónInicioDeSesiónRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    private String contraseñaAlojada;

    private Boolean cuentaActiva;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseñaIngresada;
}
