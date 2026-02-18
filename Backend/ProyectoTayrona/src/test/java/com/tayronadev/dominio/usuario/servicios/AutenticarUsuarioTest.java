package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.excepcionesUsuario.ContraseñaExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.CorreoExcepcion;
import com.tayronadev.dominio.usuario.excepcionesUsuario.InicioSesiónExcepcion;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Vamos a validar las autenticaciones de el usuario se ejecuten correctamente")
public class AutenticarUsuarioTest {

    private User user;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticadorUsuario autenticadorUsuario;

    @BeforeEach
    void setup(){
        user = new User("Erick Diaz", "erickdiazsaavedra@gmail.com","Saavedra2026", TipoUsuario.ADMINISTRADOR);
    }

    @Nested
    @DisplayName("Vamos a autenticar un usuario, validando correo, contraseña y estado de cuenta")
    class ValidaCredencialesInicioSesion {

        @Test
        @DisplayName("Permite iniciar sesión cuando las credenciales son válidas")
        void permiteIniciarSesionCuandoCredencialesSonValidas() {
            // Arrange
            String contraseñaIngresada = "Saavedra2026";

            when(passwordEncoder.matches(contraseñaIngresada, user.getContraseña()))
                    .thenReturn(true);

            // Act & Assert
            assertDoesNotThrow(() ->
                    autenticadorUsuario.validarCredenciales(
                            user.getCorreo(),
                            user.getContraseña(),
                            user.isCuentaActiva(),
                            contraseñaIngresada
                    )
            );
        }

        @Test
        @DisplayName("No permite iniciar sesión cuando el correo es null")
        void noPermiteIniciarSesionCuandoCorreoEsNull() {
            // Arrange
            String contraseñaIngresada = "Saavedra2026";

            // Act & Assert - Correo null lanza excepción
            assertThrows(CorreoExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                            null, // correo null
                            user.getContraseña(),
                            user.isCuentaActiva(),
                            contraseñaIngresada
                    )
            );
        }

        @Test
        @DisplayName("No permite iniciar sesión cuando la contraseña es inválida")
        void noPermiteIniciarSesionCuandoContraseñaEsInvalida() {
            // Arrange
            String correo = "erickdiazsaavedra@gmail.com";
            String contraseñaIngresada = "ContrasenaIncorrecta";

            when(passwordEncoder.matches(contraseñaIngresada, user.getContraseña()))
                .thenReturn(false);

            // Act & Assert
            assertThrows(ContraseñaExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                        correo,
                        user.getContraseña(),
                        true, // cuenta activa
                        contraseñaIngresada
                    )
            );
        }

        @Test
        @DisplayName("No permite iniciar sesión cuando la cuenta está inactiva")
        void noPermiteIniciarSesionCuandoCuentaEstaInactiva() {
            // Arrange
            String correo = "erickdiazsaavedra@gmail.com";
            String contraseñaIngresada = "Saavedra2026";

            // Act & Assert - La validación de cuenta inactiva ocurre antes de verificar contraseña
            assertThrows(InicioSesiónExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                        correo,
                        user.getContraseña(),
                        false, // cuenta inactiva
                        contraseñaIngresada
                    )
            );
        }
    }
}
