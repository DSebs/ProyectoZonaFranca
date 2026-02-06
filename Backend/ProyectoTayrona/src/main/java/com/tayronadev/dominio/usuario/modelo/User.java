package com.tayronadev.dominio.usuario.modelo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;

@Getter
public class User {

    private String id;
    private final String nombre;
    @Setter
    private String correo;
    @Setter(AccessLevel.NONE)
    private String contraseña;
    @Setter
    private boolean cuentaActiva;
    @Setter
    private TipoUsuario tipoUsuario;

    public User(@NonNull String nombre, @NonNull String correo,
                @NonNull String contraseña, @NonNull TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.cuentaActiva = true;
        
        // Validar que el correo y la contraseña cumplan con los requisitos
        validarCorreoElectronico(correo);
        validarComposicionContraseña(contraseña);
    }

    
    // Validar composicion de correo electronico
    public void validarCorreoElectronico(String correo) {
        if (correo == null) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_NULO);
        } else if (correo.isEmpty()) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_VACIO);
        } else if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
        }
    }

    // Validar composición de contraseña (no valida si ya está hasheada, ej. BCrypt)
    public void validarComposicionContraseña(String contraseña) {
        if (contraseña != null && contraseña.startsWith("$2")) {
            return; // Contraseña ya hasheada (BCrypt)
        }
        final int valorContraseña = 8;
        if (contraseña == null) {
            throw new ContraseñaExcepcion(ContraseñaExcepcion.MENSAJE_CONTRASEÑA_NULA);
        } else if (contraseña.isEmpty()) {
            throw new ContraseñaExcepcion(ContraseñaExcepcion.MENSAJE_CONTRASEÑA_VACIA);
        } else if (
            contraseña.length() < valorContraseña || 
            !contraseña.matches(".*[A-Z].*") ||
            !contraseña.matches(".*[0-9].*")
        ) {
            throw new ContraseñaExcepcion(ContraseñaExcepcion.MENSAJE_CONTRASEÑA_INVALIDA);
        }
     }

    // Setter personalizado para contraseña con validación
    public void setContraseña(@NonNull String contraseña) {
        this.contraseña = contraseña;
        validarComposicionContraseña(contraseña);
    }
}