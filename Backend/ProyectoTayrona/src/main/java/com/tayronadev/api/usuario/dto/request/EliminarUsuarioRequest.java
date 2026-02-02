package com.tayronadev.api.usuario.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EliminarUsuarioRequest {

    private String correo;

}
