package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.InicioSesiónExcepcion;
import com.tayronadev.dominio.usuario.modelo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Servicio de dominio para autenticación de usuarios
 */
@Service
@Slf4j
public class AutenticadorUsuario {

    /**
     * Verifica las credenciales de un usuario
     */
    public void validarCredenciales(User usuario, String contraseñaIngresada) {
        if (usuario == null) {
            log.warn("Intento de inicio de sesión con usuario inexistente");
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
        }

        if (!usuario.isCuentaActiva()) {
            log.warn("Intento de inicio de sesión con cuenta inactiva: {}", usuario.getCorreo());
            throw new InicioSesiónExcepcion("La cuenta está desactivada");
        }

        // Nota: En producción, la contraseña debe estar hasheada y compararse con BCrypt o similar
        // Por ahora, comparación simple (esto debe cambiarse)
        if (!Objects.equals(usuario.getContraseña(), contraseñaIngresada)) {
            log.warn("Intento de inicio de sesión con contraseña incorrecta para: {}", usuario.getCorreo());
            throw new ContraseñaExcepcion("Contraseña incorrecta");
        }

        log.debug("Credenciales validadas exitosamente para: {}", usuario.getCorreo());
    }

    /**
     * Determina si un usuario puede iniciar sesión (está activo)
     */
    public boolean puedeIniciarSesion(User usuario) {
        return usuario != null && usuario.isCuentaActiva();
    }
}
