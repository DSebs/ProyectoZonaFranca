package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.UsuarioNoEncontradoException;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para gestionar usuarios (activar, desactivar, actualizar)
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GestionarUsuarioUseCase {

    private final UsuarioRepositorio usuarioRepositorio;

    /**
     * Activa la cuenta de un usuario
     */
    public User activarCuenta(String usuarioId) {
        log.info("Activando cuenta del usuario: {}", usuarioId);
        User user = obtenerUsuarioPorId(usuarioId);

        user.setCuentaActiva(true);
        usuarioRepositorio.actualizarUsuario(usuarioId, user);

        log.info("Cuenta activada exitosamente para usuario: {}", usuarioId);
        return user;
    }

    /**
     * Desactiva la cuenta de un usuario
     */
    public User desactivarCuenta(String usuarioId) {
        log.info("Desactivando cuenta del usuario: {}", usuarioId);
        var usuario = obtenerUsuarioPorId(usuarioId);

        usuario.setCuentaActiva(false);
        usuarioRepositorio.actualizarUsuario(usuarioId, usuario);

        log.info("Cuenta desactivada exitosamente para usuario: {}", usuarioId);
        return usuario;
    }

    /**
     * Actualiza el tipo de usuario
     */
    public User cambiarTipoUsuario(String usuarioId, TipoUsuario nuevoTipo) {
        log.info("Cambiando tipo de usuario {} a: {}", usuarioId, nuevoTipo);
        User user = obtenerUsuarioPorId(usuarioId);

        user.setTipoUsuario(nuevoTipo);
        usuarioRepositorio.actualizarUsuario(usuarioId, user);

        log.info("Tipo de usuario cambiado exitosamente para usuario: {}", usuarioId);
        return user;
    }

    /**
     * Actualiza la contraseña de un usuario.
     * La validación de formato se hace en el setter setContraseña() de User.
     */
    public User actualizarContraseña(String usuarioId, String nuevaContraseña) {
        log.info("Actualizando contraseña del usuario: {}", usuarioId);
        User user = obtenerUsuarioPorId(usuarioId);

        // El setter setContraseña() ya valida el formato automáticamente
        user.setContraseña(nuevaContraseña);
        usuarioRepositorio.actualizarUsuario(usuarioId, user);

        log.info("Contraseña actualizada exitosamente para usuario: {}", usuarioId);
        return user;
    }

    /**
     * Elimina un usuario (eliminación lógica o física según implementación)
     */
    public void eliminarUsuario(String usuarioId) {
        log.info("Eliminando usuario: {}", usuarioId);
        User user = obtenerUsuarioPorId(usuarioId);
        usuarioRepositorio.eliminarUsuario(usuarioId);
        log.info("Usuario eliminado exitosamente: {}", usuarioId);
    }


    private User obtenerUsuarioPorId(String usuarioId) {
        return usuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
    }
}
