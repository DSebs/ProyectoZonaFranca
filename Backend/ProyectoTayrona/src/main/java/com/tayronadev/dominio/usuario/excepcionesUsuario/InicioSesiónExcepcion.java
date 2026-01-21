package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class InicioSesiónExcepcion extends RuntimeException {

    public InicioSesiónExcepcion(String message) {
        super(message);
    }

    public InicioSesiónExcepcion(String message, Throwable causa) {
        super(message, causa);
    }
}
