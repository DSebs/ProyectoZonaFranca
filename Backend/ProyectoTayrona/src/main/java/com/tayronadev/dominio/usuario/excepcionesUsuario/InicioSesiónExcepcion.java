package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class InicioSesiónExcepcion extends RuntimeException {

    public static final String INICIO_DE_SESION_INVALIDO = "Correo o contraseña incorrectas";

    public InicioSesiónExcepcion(String message) {
        super(message);
    }

    public InicioSesiónExcepcion(String message, Throwable causa) {
        super(message, causa);
    }
}
