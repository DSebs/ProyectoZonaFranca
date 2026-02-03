package com.tayronadev.dominio.usuario.excepcionesUsuario;

public class ContraseñaExcepcion extends UsuarioExcepcion{

    public static final String MENSAJE_CONTRASEÑA_INVALIDA = "La contraseña debe tener mínimo 8 caracteres, una letra mayúscula y un número";
    public static final String MENSAJE_CONTRASEÑA_VACIA = "Complete todos los campos";
    public static final String MENSAJE_CONTRASEÑA_NULA = "La contraseña no puede ser nula";
    public static final String MENSAJE_CONTRASEÑA_INCORRECTA = "La contraseña no es valida";
    
    public ContraseñaExcepcion(String mensaje) {
        super(mensaje);
    }
    
    public ContraseñaExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}