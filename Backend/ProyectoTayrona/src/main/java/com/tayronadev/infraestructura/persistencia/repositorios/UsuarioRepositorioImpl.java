package com.tayronadev.infraestructura.persistencia.repositorios;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.infraestructura.persistencia.entidades.UsuarioEntity;
import com.tayronadev.infraestructura.persistencia.mappers.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del repositorio de usuarios usando JPA
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UsuarioRepositorioImpl implements UsuarioRepositorio {
    
    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioMapper mapper;
    
    @Override
    public User guardar(User user) {
        log.debug("Guardando usuario con ID: {}", user.getId());
        var entity = mapper.toEntity(user);
        var savedEntity = jpaRepository.save(entity);
        var result = mapper.toDomain(savedEntity);
        log.debug("Usuario guardado exitosamente con ID: {}", result.getId());
        return result;
    }
    
    @Override
    public void actualizarUsuario(User user) {
        log.debug("Actualizando usuario con ID: {}", user.getId());
        var entity = mapper.toEntity(user);
        jpaRepository.save(entity);
        log.debug("Usuario actualizado exitosamente con ID: {}", user.getId());
    }
    
    @Override
    public void eliminarUsuario(String id) {
        log.warn("Eliminando usuario con ID: {}", id);
        jpaRepository.deleteById(id);
        log.debug("Usuario eliminado exitosamente con ID: {}", id);
    }
    
    @Override
    public Optional<User> buscarPorId(String id) {
        log.debug("Buscando usuario por ID: {}", id);
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> obtenerPorCorreo(String correo) {
        log.debug("Buscando usuario por correo: {}", correo);
        return jpaRepository.findByCorreo(correo)
                .map(mapper::toDomain);
    }
    
    @Override
    public boolean existePorCorreo(String correo) {
        log.debug("Verificando existencia de usuario con correo: {}", correo);
        return jpaRepository.existsByCorreo(correo);
    }
    
    @Override
    public boolean existePorId(String id) {
        log.debug("Verificando existencia de usuario con ID: {}", id);
        return jpaRepository.existsById(id);
    }
    
    @Override
    public List<User> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> buscarPorTipo(TipoUsuario tipoUsuario) {
        log.debug("Buscando usuarios por tipo: {}", tipoUsuario);
        return jpaRepository.findByTipoUsuario(tipoUsuario)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> buscarActivos() {
        log.debug("Buscando usuarios activos");
        return jpaRepository.findByCuentaActivaTrue()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> buscarInactivos() {
        log.debug("Buscando usuarios inactivos");
        return jpaRepository.findByCuentaActivaFalse()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> obtenerCorreos() {
        log.debug("Obteniendo lista de correos electrónicos");
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioEntity::getCorreo)
                .collect(Collectors.toList());
    }
}
