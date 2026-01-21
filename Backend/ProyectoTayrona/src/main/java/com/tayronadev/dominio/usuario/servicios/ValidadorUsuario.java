package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.modelo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio para validar reglas de negocio que requieren contexto externo.
 * 
 * NOTA: Las validaciones de formato (correo, contraseña) están en la clase User.
 * Este servicio maneja validaciones que requieren datos externos (duplicados, etc.)
 */
@Service
@Slf4j
public class ValidadorUsuario {

    /**
     * Valida que un correo no esté duplicado en la lista proporcionada.
     * Esta validación requiere contexto externo (lista de correos existentes).
     */
    public void validarCorreoNoDuplicado(String correo, List<String> correosExistentes) {
        if (correosExistentes.contains(correo)) {
            log.warn("Intento de crear usuario con correo duplicado: {}", correo);
            throw new CorreoExcepcion(CorreoExcepcion.MENSAJE_CORREO_YA_REGISTRADO);
        }
    }

    /**
     * Valida que un usuario pueda ser creado (valida duplicados).
     * Las validaciones de formato se hacen en el constructor de User.
     */
    public void validarCreacionUsuario(String correo, List<String> correosExistentes) {
        log.debug("Validando creación de usuario con correo: {}", correo);
        validarCorreoNoDuplicado(correo, correosExistentes);
        log.debug("Validaciones de creación de usuario completadas exitosamente");
    }

    /**
     * Valida que un usuario pueda ser actualizado (valida duplicados si el correo cambió).
     * Las validaciones de formato se hacen en los setters de User.
     */
    public void validarActualizacionUsuario(User usuarioExistente, String nuevoCorreo, List<String> correosExistentes) {
        log.debug("Validando actualización de usuario con ID: {}", usuarioExistente.getId());
        
        // Si el correo cambió, validar que no esté duplicado
        if (!usuarioExistente.getCorreo().equals(nuevoCorreo)) {
            validarCorreoNoDuplicado(nuevoCorreo, correosExistentes);
        }
        
        log.debug("Validaciones de actualización completadas exitosamente");
    }
}
