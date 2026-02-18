package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.modelo.User;

import java.util.Optional;

/**
 * Puerto (interfaz) para obtener el usuario actualmente autenticado.
 * Esta abstracción permite desacoplar el dominio de la implementación
 * específica de seguridad (Spring Security).
 */
public interface UsuarioActualService {
    
    /**
     * Obtiene el usuario actualmente autenticado.
     * @return Optional con el usuario si está autenticado, vacío si no.
     */
    Optional<User> obtenerUsuarioActual();
    
    /**
     * Obtiene el ID del usuario actualmente autenticado.
     * @return Optional con el ID del usuario si está autenticado.
     */
    Optional<String> obtenerUsuarioIdActual();
    
    /**
     * Verifica si hay un usuario autenticado.
     */
    boolean hayUsuarioAutenticado();
}
