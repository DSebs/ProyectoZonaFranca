package com.tayronadev.dominio.usuario.modelo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.InicioSesiónExcepcion;

import java.util.List;

@Getter
public class User {

    private final String id;
    private final String nombre;
    private final String correo;
    @Setter(AccessLevel.NONE)
    private String contraseña;
    private boolean cuentaActiva;
    @Setter
    private TipoUsuario tipoUsuario;

    public User(@NonNull String id, @NonNull String nombre, @NonNull String correo, 
                @NonNull String contraseña, @NonNull TipoUsuario tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
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

    //Validar si correo inicio de sesión:
    public void correoExistente(List<String> lista, String correo){
        if (!lista.contains(correo)) {
            throw new InicioSesiónExcepcion(InicioSesiónExcepcion.INICIO_DE_SESION_INVALIDO);
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

    //Validar si correo y contraseña existe: 
   public void contraseñaExistente(List<String> lista, String contraseña){
            if (!lista.contains(contraseña)) {
                throw new InicioSesiónExcepcion(InicioSesiónExcepcion.INICIO_DE_SESION_INVALIDO);
            }
    }

    // Validación que se hara accediendo
    //a la base de datos, validar con sebas si hacemos el metodo en el controller
    //public boolean verificarContraseña(String contraseñaAVerificar) {
    //    return Objects.equals(this.contraseña, contraseñaAVerificar);
    //}

    // Setter personalizado para contraseña con validación
    public void setContraseña(@NonNull String contraseña) {
        this.contraseña = contraseña;
        validarComposicionContraseña();
    }


}