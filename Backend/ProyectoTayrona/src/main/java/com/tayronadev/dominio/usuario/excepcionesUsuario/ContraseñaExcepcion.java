package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class ContraseñaExcepcion extends UsuarioExcepcion{

    public static final String MENSAJE_CONTRASEÑA_INVALIDA = "La contraseña debe tener mas de 8 caracteres\n" + "Un numero y una letra mayuscula";
    public static final String MENSAJE_CONTRASEÑA_VACIA = "La contraseña no puede estar vacia";
    public static final String MENSAJE_CONTRASEÑA_NULA = "La contraseña no puede ser nula";
    public static final String MENSAJE_CONTRASEÑA_NO_EXISTE = "La contraseña no existe en el sistema";
    
    public ContraseñaExcepcion(String mensaje) {
        super(mensaje);
    }
    
    public ContraseñaExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}