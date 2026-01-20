package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class CorreoExcepcion extends UsuarioExcepcion {

    public static final String MENSAJE_CORREO_INVALIDO = "El correo electronico no es valido";
    public static final String MENSAJE_CORREO_VACIO = "Complete todos los campos";
    public static final String MENSAJE_CORREO_NULO = "El correo electronico no puede ser nulo";
    
    public CorreoExcepcion(String mensaje) {
        super(mensaje);
    }
    
    public CorreoExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}