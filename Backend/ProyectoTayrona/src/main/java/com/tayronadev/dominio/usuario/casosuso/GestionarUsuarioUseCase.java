package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
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
    public User actualizarUsuario(String correo, String nuevaContraseña, TipoUsuario nuevoTipo, Boolean estado) {
        log.info("Actualizando usuario: {}", correo);
        User user = obtenerUsuarioPorCorreo(correo);


        user.setContraseña(passwordEncoder.encode(nuevaContraseña));
        user.setTipoUsuario(nuevoTipo);
        user.setCuentaActiva(estado);

        usuarioRepositorio.actualizarUsuario(user);

        log.info("Contraseña actualizada exitosamente para usuario: {}", correo);
        return user;
    }

    /**
     * Elimina un usuario (eliminación lógica o física según implementación)
     */
    public String eliminarUsuario(String correo) {
        log.info("Eliminando usuario: {}", correo);
        User user = obtenerUsuarioPorCorreo(correo);
        usuarioRepositorio.eliminarUsuario(correo);
        log.info("Usuario eliminado exitosamente: {}", correo);
        return correo;
    }

    /**
     * Metodos de usuario)
     */
    public User obtenerUsuarioPorCorreo(String correo){
        return usuarioRepositorio.obtenerPorCorreo(correo)
                .orElseThrow(() -> new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO));
    }
}