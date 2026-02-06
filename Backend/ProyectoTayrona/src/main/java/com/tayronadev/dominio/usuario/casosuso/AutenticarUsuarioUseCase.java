package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.AutenticadorUsuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para autenticar usuarios (inicio de sesión)
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AutenticarUsuarioUseCase {

    private final UsuarioRepositorio usuarioRepositorio;
    private final AutenticadorUsuario autenticadorUsuario;


    public User ejecutarInicioSesion(String correo, String contraseñaActual, Boolean cuentaActiva, String contraseñaIngresada) {
        log.info("Iniciando autenticación para correo: {}", correo);

        User user = usuarioRepositorio.obtenerPorCorreo(correo)
                .orElseThrow(() -> {
                    log.warn("Intento de autenticación con correo inexistente: {}", correo);
                    return new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
                });

        autenticadorUsuario.validarCredenciales(correo, contraseñaActual, cuentaActiva, contraseñaIngresada);

        log.info("Autenticación exitosa para usuario: {}", correo);

        return user;
    }

    /**
     * Verifica si un usuario puede iniciar sesión (usuario existe y está activo)
     */
    public boolean puedeIniciarSesion(String correo) {
        return usuarioRepositorio.obtenerPorCorreo(correo)
                .map(autenticadorUsuario::puedeIniciarSesion)
                .orElse(false);
    }
}
