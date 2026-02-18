package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidadorUsuario - Servicio de Validación de Usuario")
public class ValidadoUsuarioTest {
    
    private ValidadorUsuario validadorUsuario;

    @BeforeEach
    void setUp() {
        validadorUsuario = new ValidadorUsuario();
    }

    @Nested
    @DisplayName("Validación de correo no duplicado")
    class ValidaCorreoNoDuplicado {
        
        @Test
        @DisplayName("Permite correo cuando no existe en la lista")
        void permiteCorreoCuandoNoExiste() {
            // Arrange
            String correo = "nuevo@email.com";
            List<String> correosExistentes = Arrays.asList("otro@email.com", "diferente@email.com");
            
            // Act & Assert
            assertDoesNotThrow(() -> 
                validadorUsuario.validarCorreoNoDuplicado(correo, correosExistentes)
            );
        }
        
        @Test
        @DisplayName("Lanza excepción cuando el correo ya existe")
        void lanzaExcepcionCuandoCorreoExiste() {
            // Arrange
            String correo = "existente@email.com";
            List<String> correosExistentes = Arrays.asList("existente@email.com", "otro@email.com");
            
            // Act & Assert
            assertThrows(CorreoExcepcion.class, () -> 
                validadorUsuario.validarCorreoNoDuplicado(correo, correosExistentes)
            );
        }
        
        @Test
        @DisplayName("Permite correo cuando la lista está vacía")
        void permiteCorreoCuandoListaVacia() {
            // Arrange
            String correo = "nuevo@email.com";
            List<String> correosExistentes = Collections.emptyList();
            
            // Act & Assert
            assertDoesNotThrow(() -> 
                validadorUsuario.validarCorreoNoDuplicado(correo, correosExistentes)
            );
        }
    }

    @Nested
    @DisplayName("Validación de creación de usuario")
    class ValidaCreacionUsuario {
        
        @Test
        @DisplayName("Permite crear usuario con correo único")
        void permiteCrearUsuarioConCorreoUnico() {
            // Arrange
            String correo = "nuevo@email.com";
            List<String> correosExistentes = Arrays.asList("otro@email.com");
            
            // Act & Assert
            assertDoesNotThrow(() -> 
                validadorUsuario.validarCreacionUsuario(correo, correosExistentes)
            );
        }
        
        @Test
        @DisplayName("No permite crear usuario con correo duplicado")
        void noPermiteCrearUsuarioConCorreoDuplicado() {
            // Arrange
            String correo = "existente@email.com";
            List<String> correosExistentes = Arrays.asList("existente@email.com");
            
            // Act & Assert
            assertThrows(CorreoExcepcion.class, () -> 
                validadorUsuario.validarCreacionUsuario(correo, correosExistentes)
            );
        }
    }
}
