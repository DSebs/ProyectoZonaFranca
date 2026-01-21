package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.infraestructura.persistencia.entidades.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para entidades de usuario
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
    
    /**
     * Busca un usuario por su correo electrónico
     */
    Optional<UsuarioEntity> findByCorreo(String correo);
    
    /**
     * Verifica si existe un usuario con el correo especificado
     */
    boolean existsByCorreo(String correo);
    
    /**
     * Busca usuarios por tipo de usuario
     */
    List<UsuarioEntity> findByTipoUsuario(TipoUsuario tipoUsuario);
    
    /**
     * Busca usuarios activos (cuentaActiva = true)
     */
    List<UsuarioEntity> findByCuentaActivaTrue();
    
    /**
     * Busca usuarios inactivos (cuentaActiva = false)
     */
    List<UsuarioEntity> findByCuentaActivaFalse();
    
    /**
     * Busca usuarios por tipo y estado de cuenta
     */
    List<UsuarioEntity> findByTipoUsuarioAndCuentaActiva(TipoUsuario tipoUsuario, boolean cuentaActiva);
}
