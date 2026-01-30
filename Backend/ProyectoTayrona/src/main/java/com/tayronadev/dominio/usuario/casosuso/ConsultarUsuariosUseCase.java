package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para consultar usuarios
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ConsultarUsuariosUseCase {

    private final UsuarioRepositorio usuarioRepositorio;

    /**
     * Busca un usuario por su ID
     */
    /**
     * public User buscarPorId(String usuarioId) {
        log.debug("Buscando usuario con ID: {}", usuarioId);
        return usuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
    }
     */

    /**
     * Busca un usuario por su correo electrónico
     */

    public User obtenerUsuarioPorCorreo(String correo){
        log.debug("Buscando usuario con correo: {}", correo);
        return usuarioRepositorio.obtenerPorCorreo(correo)
                .orElseThrow(() -> new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_INVALIDO));
    }

    /**
     * Obtiene todos los usuarios
     */
    public List<User> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioRepositorio.obtenerTodos();
    }

    /**
     * Busca usuarios por tipo
     */
    public List<User> buscarPorTipo(TipoUsuario tipoUsuario) {
        log.debug("Buscando usuarios por tipo: {}", tipoUsuario);
        return usuarioRepositorio.buscarPorTipo(tipoUsuario);
    }

    /**
     * Obtiene usuarios activos
     */
    public List<User> obtenerUsuariosActivos() {
        log.debug("Obteniendo usuarios activos");
        return usuarioRepositorio.buscarActivos();
    }

    /**
     * Obtiene usuarios inactivos
     */
    public List<User> obtenerUsuariosInactivos() {
        log.debug("Obteniendo usuarios inactivos");
        return usuarioRepositorio.buscarInactivos();
    }

    /**
     * Verifica si existe un usuario con el ID especificado
     */
    public boolean existeUsuario(String usuarioId) {
        return usuarioRepositorio.existePorId(usuarioId);
    }

    /**
     * Verifica si existe un usuario con el correo especificado
     */
    public boolean existePorCorreo(String correo) {
        return usuarioRepositorio.existePorCorreo(correo);
    }

    /**
     * Obtiene la cantidad total de usuarios
     */
    public long contarUsuarios() {
        log.debug("Contando usuarios");
        return usuarioRepositorio.obtenerTodos().size();
    }

    /**
     * Obtiene la cantidad de usuarios activos
     */
    public long contarUsuariosActivos() {
        log.debug("Contando usuarios activos");
        return usuarioRepositorio.buscarActivos().size();
    }

    /**
     * Obtiene la cantidad de usuarios inactivos
     */
    public long contarUsuariosInactivos() {
        log.debug("Contando usuarios inactivos");
        return usuarioRepositorio.buscarInactivos().size();
    }
}
