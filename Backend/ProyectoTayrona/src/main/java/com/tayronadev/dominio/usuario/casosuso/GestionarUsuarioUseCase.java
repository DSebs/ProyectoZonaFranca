package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.UsuarioNoEncontradoException;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.ValidadorUsuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para gestionar usuarios (activar, desactivar, actualizar)
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GestionarUsuarioUseCase {

    private final UsuarioRepositorio usuarioRepositorio;
    private final ValidadorUsuario validadorUsuario;
    private final PasswordEncoder passwordEncoder;

    /**
     * Activa la cuenta de un usuario
     */
    public User activarCuenta(String correo) {
        return cambiarEstadoCuenta(correo, true);
    }

    /**
     * Desactiva la cuenta de un usuario
     */
    public User desactivarCuenta(String correo) {
        return cambiarEstadoCuenta(correo, false);
    }

    /**
     * Actualiza el tipo de usuario
     */
    public User cambiarTipoUsuario(String correo, TipoUsuario nuevoTipo) {
        log.info("Cambiando tipo de usuario {} a: {}", correo, nuevoTipo);
        User user = obtenerUsuarioPorCorreo(correo);

        user.setTipoUsuario(nuevoTipo);
        usuarioRepositorio.actualizarUsuario(user);

        log.info("Tipo de usuario cambiado exitosamente para usuario: {}", correo);
        return user;
    }

    /**
     * Actualiza el correo de un usuario.
     * Valida que no esté duplicado usando ValidadorUsuario.
     */
    public User actualizarCorreo(String correoActual, String nuevoCorreo, List<String> correosExistentes) {
        log.info("Actualizando correo del usuario: {}", correoActual);

        User user = obtenerUsuarioPorCorreo(correoActual);

        // Validar duplicados
        validadorUsuario.validarActualizacionUsuario(user, nuevoCorreo, correosExistentes);

        // Actualizar correo
        user.setCorreo(nuevoCorreo);
        usuarioRepositorio.actualizarUsuario(user);

        log.info("Correo actualizado exitosamente para usuario: {}", nuevoCorreo);
        return user;
    }


    /**
     * Actualiza la contraseña de un usuario.
     * La validación de formato se hace en el setter setContraseña() de User.
     */
    public User actualizarUsuario(String correo, String nuevaContraseña) {
        log.info("Actualizando usuario: {}", correo);
        User user = obtenerUsuarioPorCorreo(correo);

        // Hashear la contraseña antes de asignar; el setter valida formato
        user.setContraseña(passwordEncoder.encode(nuevaContraseña));
        usuarioRepositorio.actualizarUsuario(user);

        log.info("Contraseña actualizada exitosamente para usuario: {}", correo);
        return user;
    }

    /**
     * Elimina un usuario (eliminación lógica o física según implementación)
     */
    public void eliminarUsuario(String correo) {
        log.info("Eliminando usuario: {}", correo);
        User user = obtenerUsuarioPorCorreo(correo);
        usuarioRepositorio.eliminarUsuario(user.getId());
        log.info("Usuario eliminado exitosamente: {}", correo);
    }

    /**
     * Metodos de usuario)
     */


    public User obtenerUsuarioPorCorreo(String correo){
        return usuarioRepositorio.obtenerPorCorreo(correo)
                .orElseThrow(() -> new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO));
    }

    private User cambiarEstadoCuenta(String correo, boolean estado) {
        log.info("Cambiando estado de cuenta para el usuario: {} -> {}", correo, estado);

        User user = obtenerUsuarioPorCorreo(correo);

        user.setCuentaActiva(estado);
        usuarioRepositorio.actualizarUsuario(user);

        log.info("Estado de cuenta actualizado correctamente para: {}", correo);
        return user;
    }
}