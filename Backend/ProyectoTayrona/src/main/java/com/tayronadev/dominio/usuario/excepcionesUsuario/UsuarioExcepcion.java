package com.tayronadev.dominio.usuario.excepcionesUsuario;

public abstract class UsuarioExcepcion extends RuntimeException {

    protected UsuarioExcepcion(String mensaje) {
        super(mensaje);
    }

    protected UsuarioExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}