package com.tayronadev.dominio.usuario.modelo;

import java.util.Objects;
import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;

public class User {

    private final String id;
    private final String nombre;
    private final String correo;
    private String contraseña;
    private boolean cuentaActiva;
    private TipoUsuario tipoUsuario;

    public User(String id, String nombre, String correo, String contraseña, TipoUsuario tipoUsuario) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        this.correo = Objects.requireNonNull(correo, "El email es obligatorio");
        this.contraseña = Objects.requireNonNull(contraseña, "La contraseña es obligatoria");
        this.tipoUsuario = Objects.requireNonNull(tipoUsuario, "El tipo de usuario es obligatorio");
        this.cuentaActiva = true;
        
        // Validar que el correo y la contraseña cumplan con los requisitos
        validarCorreoElectronico();
        validarComposicionContraseña();
    }

    
    // Validar composicion de correo electronico
    public void validarCorreoElectronico() {
        if (correo == null) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_NULO);
        } else if (correo.isEmpty()) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_VACIO);
        } else if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
        }
    }

    // Validar composición de contraseña
    public void validarComposicionContraseña() {
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

     //Validar si correo y contraseña existe: Validación que se hara accediendo
     //a la base de datos, validar con sebas si hacemos el metodo en el controller
    //public boolean verificarContraseña(String contraseñaAVerificar) {
    //    return Objects.equals(this.contraseña, contraseñaAVerificar);
    //}

     

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContraseña() {
        return contraseña;
    }
    
    public boolean isCuentaActiva() {
        return cuentaActiva;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    // Setters 
    public void setContraseña(String contraseña) {
        this.contraseña = Objects.requireNonNull(contraseña, "La contraseña es obligatoria");
        validarComposicionContraseña();
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }


}