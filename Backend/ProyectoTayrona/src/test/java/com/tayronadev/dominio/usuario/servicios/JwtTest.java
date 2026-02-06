package com.tayronadev.dominio.usuario.servicios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class) 
@DisplayName("JwtTest - Servicio de JWT")
public class JwtTest {

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = new Jwt();
    }
}

@Nested
@DisplayName("Genera un token JWT válido")
class GeneraTokenJwtValido {
    @Test
    @DisplayName("Genera un token JWT válido")
    void generaTokenJwtValido() {
        // Arrange
        User user = new User("Erick Diaz", "erickdiazsaavedra@gmail.com", "Saavedra2025", TipoUsuario.ADMINISTRADOR);
        
        // Act
        String token = jwt.generarToken(user);
        
        // Assert
        assertNotNull(token);
    }
}
