package com.tayronadev.dominio.usuario.servicios;

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
        @DisplayName("No permite iniciar sesión cuando el correo es inválido")
        void noPermiteIniciarSesionCuandoCorreoEsInvalido() {
            // Arrange
            String correoInvalido = "correo-invalido";
            String contraseñaIngresada = "Saavedra2026";
            
            when(usuarioRepositorio.obtenerPorCorreo(correoInvalido))
                .thenReturn(Optional.empty());

            when(passwordEncoder.matches(contraseñaIngresada, user.getContraseña()))
                .thenReturn(true);

            assertThrows(CorreoExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                            correoInvalido,
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
            String contraseñaIngresada = "Saavedra2026";
            
            when(usuarioRepositorio.obtenerPorCorreo(correo))
                .thenReturn(Optional.of(user));

            when(passwordEncoder.matches(contraseñaIngresada, user.getContraseña()))
                .thenReturn(false);

            assertThrows(ContraseñaExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                        correo,
                        user.getContraseña(),
                        user.isCuentaActiva(),
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
            
            when(usuarioRepositorio.obtenerPorCorreo(correo))
                .thenReturn(Optional.of(user));

            when(passwordEncoder.matches(contraseñaIngresada, user.getContraseña()))
                .thenReturn(true);

            assertThrows(InicioSesiónExcepcion.class, () ->
                    autenticadorUsuario.validarCredenciales(
                        correo,
                        user.getContraseña(),
                        false,
                        contraseñaIngresada
                    )
            );
        }
    }
}
