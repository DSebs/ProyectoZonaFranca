package com.tayronadev.dominio.usuario.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario - entidad dominio")
public class UsuarioTest {

    User user;

    @BeforeEach
    void setup(){
        user = new User("Erick Diaz", "erickdiazsaavedra@gmail.com","Saavedra2025", TipoUsuario.ADMINISTRADOR);
    }

    @Nested
    @DisplayName("Creara un usuario correctamente")
    class CrearUsuario {

        @Test
        @DisplayName("Validamos que las reglas de composición de correo pasen perfectamente")
        void noDebeLanzarExcepcionSiCorreoEsValido() {
            assertDoesNotThrow(() ->
                    user.validarCorreoElectronico(user.getCorreo())
            );
        }

        @Test
        @DisplayName("Validamos que las reglas de composicion de contraseña pasen perfectamente")
        void noDebeLanzarExcepcionSicontraseñaEsValido() {
            assertDoesNotThrow(() ->
                    user.validarComposicionContraseña(user.getContraseña()));
        }
    }
}
