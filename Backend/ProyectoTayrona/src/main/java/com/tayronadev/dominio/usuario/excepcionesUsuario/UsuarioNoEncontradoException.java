package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class UsuarioNoEncontradoException extends RuntimeException {

    private static final String MENSAJE = "Usuario no encontrado";

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

}
