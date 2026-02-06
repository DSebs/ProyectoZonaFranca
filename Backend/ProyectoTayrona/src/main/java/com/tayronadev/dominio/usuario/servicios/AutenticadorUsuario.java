package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.InicioSesiónExcepcion;
import com.tayronadev.dominio.usuario.modelo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Servicio de dominio para autenticación de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticadorUsuario {

    private PasswordEncoder passwordEncoder;
    /**
     * Verifica las credenciales de un usuario
     */
    public void validarCredenciales(String correo, String contraseñaActual, Boolean cuentaActiva, String contraseñaIngresada) {
        if (correo == null) {
            log.warn("Intento de inicio de sesión con usuario inexistente");
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
        }

        if (!cuentaActiva) {
            log.warn("Intento de inicio de sesión con cuenta inactiva: {}", correo);
            throw new InicioSesiónExcepcion(InicioSesiónExcepcion.CUENTA_INACTIVA);
        }

        // Nota: En producción, la contraseña debe estar hasheada y compararse con BCrypt o similar
        // Por ahora, comparación simple (esto debe cambiarse)
        if (!passwordEncoder.matches(contraseñaIngresada, contraseñaActual)) {
            log.warn("Intento de inicio de sesión con contraseña incorrecta para: {}", correo);
            throw new ContraseñaExcepcion(ContraseñaExcepcion.MENSAJE_CONTRASEÑA_INCORRECTA);
        }

        log.debug("Credenciales validadas exitosamente para: {}", correo);
    }

    /**
     * Determina si un usuario puede iniciar sesión (está activo)
     */
    public boolean puedeIniciarSesion(User usuario) {
        return usuario != null && usuario.isCuentaActiva();
    }
}
