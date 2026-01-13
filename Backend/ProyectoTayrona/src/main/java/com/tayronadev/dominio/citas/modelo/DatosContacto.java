package com.tayronadev.dominio.citas.modelo;

import java.util.Objects;

/**
 * Value Object que representa los datos de contacto de una persona
 */
public record DatosContacto(
    String nombre,
    String email,
    String telefono
) {
    
    public DatosContacto {
        Objects.requireNonNull(nombre, "El nombre es obligatorio");
        Objects.requireNonNull(email, "El email es obligatorio");
        Objects.requireNonNull(telefono, "El teléfono es obligatorio");
        
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!esEmailValido(email)) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        if (telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
    }
    
    private static boolean esEmailValido(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}