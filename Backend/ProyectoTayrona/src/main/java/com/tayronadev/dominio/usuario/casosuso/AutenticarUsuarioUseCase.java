package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
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

    /**
     * Autentica un usuario con correo y contraseña
     */
    public User ejecutar(String correo, String contraseña) {
        log.info("Iniciando autenticación para correo: {}", correo);

        // Buscar usuario por correo
        User user = usuarioRepositorio.obtenerPorCorreo(correo);
        
        if (user == null) {
            log.warn("Intento de autenticación con correo inexistente: {}", correo);
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO);
        }

        // Validar credenciales
        autenticadorUsuario.validarCredenciales(user, contraseña);

        log.info("Autenticación exitosa para usuario: {} ({})", user.getNombre(), correo);
        return user;
    }

    /**
     * Verifica si un usuario puede iniciar sesión (usuario existe y está activo)
     */
    public boolean puedeIniciarSesion(String correo) {
        try {
            User user = usuarioRepositorio.obtenerPorCorreo(correo);
            return autenticadorUsuario.puedeIniciarSesion(user);
        } catch (Exception e) {
            log.debug("Usuario no puede iniciar sesión: {}", e.getMessage());
            return false;
        }
    }
}
