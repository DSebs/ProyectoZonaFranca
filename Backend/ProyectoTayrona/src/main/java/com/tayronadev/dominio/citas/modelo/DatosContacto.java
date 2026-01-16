package com.tayronadev.dominio.citas.modelo;

import lombok.NonNull;
import lombok.Value;

/**
 * Value Object que representa los datos de contacto de una persona
 */
@Value
public class DatosContacto {
    @NonNull String nombre;
    @NonNull String email;
    @NonNull String telefono;
    
    public DatosContacto(@NonNull String nombre, @NonNull String email, @NonNull String telefono) {
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!esEmailValido(email)) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        if (telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    private static boolean esEmailValido(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}