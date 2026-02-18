package com.tayronadev.infraestructura.security;

import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.UsuarioActualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación del servicio para obtener el usuario actual
 * desde el contexto de seguridad de Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioActualServiceImpl implements UsuarioActualService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    
    @Override
    public Optional<User> obtenerUsuarioActual() {
        return obtenerUsuarioIdActual()
            .flatMap(usuarioRepositorio::buscarPorId);
    }
    
    @Override
    public Optional<String> obtenerUsuarioIdActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("No hay usuario autenticado en el contexto de seguridad");
            return Optional.empty();
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof String userId && !userId.equals("anonymousUser")) {
            return Optional.of(userId);
        }
        
        // Si el principal es el nombre de usuario (email), buscar por correo
        if (principal instanceof String email && email.contains("@")) {
            return usuarioRepositorio.obtenerPorCorreo(email)
                .map(User::getId);
        }
        
        log.debug("No se pudo obtener el ID del usuario del principal: {}", principal);
        return Optional.empty();
    }
    
    @Override
    public boolean hayUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
}
