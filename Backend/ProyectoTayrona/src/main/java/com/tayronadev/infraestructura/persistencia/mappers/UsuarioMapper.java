package com.tayronadev.infraestructura.persistencia.mappers;

import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.infraestructura.persistencia.entidades.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio y entidades JPA para usuarios
 */
@Component
public class UsuarioMapper {
    
    /**
     * Convierte una entidad de dominio User a entidad JPA UsuarioEntity
     */
    public UsuarioEntity toEntity(User user) {
        if (user == null) return null;
        
        var entity = new UsuarioEntity();

        entity.setNombre(user.getNombre());
        entity.setCorreo(user.getCorreo());
        entity.setContraseña(user.getContraseña());
        entity.setCuentaActiva(user.isCuentaActiva());
        entity.setTipoUsuario(user.getTipoUsuario());
        
        return entity;
    }
    
    /**
     * Convierte una entidad JPA UsuarioEntity a entidad de dominio User
     */
    public User toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        
        // Crear usuario usando constructor (las validaciones se ejecutarán)
        // Nota: El constructor valida correo y contraseña, pero estos datos ya están validados
        // al estar en la base de datos. Si necesitamos evitar validaciones en reconstrucción,
        // se podría considerar agregar un constructor de reconstrucción en User
        return new User(
            entity.getNombre(),
            entity.getCorreo(),
            entity.getContraseña(),
            entity.getTipoUsuario()
        );
    }
}
