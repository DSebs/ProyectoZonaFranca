package com.tayronadev.dominio.usuario.casosuso;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.ValidadorUsuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear un nuevo usuario
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CrearUsuarioUseCase {

    private final UsuarioRepositorio usuarioRepositorio;
    private final ValidadorUsuario validadorUsuario;
    private final PasswordEncoder passwordEncoder;


    public User ejecutarCreaciónUsuario(String nombre, String correo, String contraseña, TipoUsuario tipoUsuario) {
        log.info("Iniciando creación de usuario con correo: {} - Tipo: {}", correo, tipoUsuario);

        // Obtener correos existentes para validar duplicados (requiere contexto externo)
        var correosExistentes = usuarioRepositorio.obtenerCorreos();
        
        // Validar duplicados (esta validación requiere datos externos)
        validadorUsuario.validarCreacionUsuario(correo, correosExistentes);

        // Crear el nuevo usuario (el constructor ya valida formato de correo y contraseña)
        var nuevoUsuario = new User(
                nombre,
                correo,
                passwordEncoder.encode(contraseña),
                tipoUsuario
        );

        // Guardar en repositorio
        var usuarioGuardado = usuarioRepositorio.guardar(nuevoUsuario);

        log.info("Usuario creado exitosamente con  correo: {}", correo);

        return usuarioGuardado;
    }

    /**
     * Valida si un correo está disponible sin crear el usuario
     */
    @Transactional(readOnly = true)
    public boolean validarCorreoDisponible(String correo) {
        try {
            var correosExistentes = usuarioRepositorio.obtenerCorreos();
            // Solo validar duplicados (el formato se valida al crear el objeto User)
            validadorUsuario.validarCorreoNoDuplicado(correo, correosExistentes);
            return true;
        } catch (Exception e) {
            log.debug("Correo no disponible: {}", e.getMessage());
            return false;
        }
    }
}
