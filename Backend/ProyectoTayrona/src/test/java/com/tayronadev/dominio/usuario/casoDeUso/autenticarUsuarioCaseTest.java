package com.tayronadev.dominio.usuario.casoDeUso;


import com.tayronadev.dominio.usuario.casosuso.AutenticarUsuarioUseCase;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.AutenticadorUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Autenticar usuario - caso de uso")
public class autenticarUsuarioCaseTest {

    private User user;

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private AutenticadorUsuario autenticadorUsuario;

    @InjectMocks
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @BeforeEach
    void setUp() {
        user = new User("Erick Diaz", "erickdiazsaavedra@gmail.com", "S@ntiago2025", TipoUsuario.ADMINISTRADOR);
    }

    @Nested
    @DisplayName("Vamos a autenticar a un usuario ya creado")
    class loginUsuario{

        @Test
        @DisplayName("Vamos a hacer que un usuario inicie sesion correctamente")
        void debeDejarAccederUsuarios(){

            when(usuarioRepositorio.obtenerPorCorreo(user.getCorreo())).thenReturn(Optional.of(user));

            User resultado = autenticarUsuarioUseCase.ejecutarInicioSesion(
                    user.getCorreo(),
                    user.getContraseña(),
                    user.isCuentaActiva(),
                    "S@ntiago2026");

            assertNotNull(resultado);
        }
    }
}
