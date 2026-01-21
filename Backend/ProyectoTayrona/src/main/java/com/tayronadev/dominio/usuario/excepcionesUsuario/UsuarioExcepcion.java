package com.tayronadev.dominio.usuario.excepcionesUsuario;

public abstract class UsuarioExcepcion extends RuntimeException {

    private static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";

    protected UsuarioExcepcion(String mensaje) {
        super(mensaje);
    }

    protected UsuarioExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}