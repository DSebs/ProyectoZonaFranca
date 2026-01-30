package com.tayronadev.dominio.usuario.repositorios;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interface) del repositorio de usuarios.
 * Define las operaciones de persistencia necesarias para el dominio.
 */
public interface UsuarioRepositorio {

    /**
     * Guarda un nuevo usuario o actualiza uno existente
     */
    User guardar(User user);

    /**
     * Crea un nuevo usuario (alias de guardar para compatibilidad)
     */
    User crearUsuario(User user);

    /**
     * Actualiza un usuario existente (persiste el estado actual del User)
     */
    void actualizarUsuario(User user);

    /**
     * Elimina un usuario
     */
    void eliminarUsuario(String id);

    /**
     * Busca un usuario por su ID
     */
    Optional<User> buscarPorId(String id);

    /**
     * Obtiene un usuario por su correo electrónico
     */
    Optional<User> obtenerPorCorreo(String correo);

    /**
     * Verifica si existe un usuario con el correo especificado
     */
    boolean existePorCorreo(String correo);

    /**
     * Verifica si existe un usuario con el ID especificado
     */
    boolean existePorId(String id);

    /**
     * Obtiene todos los usuarios
     */
    List<User> obtenerTodos();

    /**
     * Obtiene usuarios por tipo
     */
    List<User> buscarPorTipo(TipoUsuario tipoUsuario);

    /**
     * Obtiene usuarios activos
     */
    List<User> buscarActivos();

    /**
     * Obtiene usuarios inactivos
     */
    List<User> buscarInactivos();

    /**
     * Obtiene lista de correos electrónicos (para compatibilidad)
     */
    List<String> obtenerCorreos();


}
