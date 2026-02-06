package com.tayronadev.dominio.usuario.casoDeUso;


import com.tayronadev.dominio.usuario.casosuso.CrearUsuarioUseCase;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.repositorios.UsuarioRepositorio;
import com.tayronadev.dominio.usuario.servicios.ValidadorUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearUsuarioCase - Caso de Uso")
public class CrearUsuarioCaseTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private ValidadorUsuario validadorUsuario;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User("Erick Diaz", "erickdiazsaavedr@qgmail.com", "S@ntiago2025", TipoUsuario.ADMINISTRADOR);
    }

    @Nested
    @DisplayName("Creara un usuario nuevo")
    class CrearUsuarioNuevo{

        @Test
        @DisplayName("Creará un usuario con los parametros de correo y contraseña")
        void debeCrearUsuarioConParametrosDeCorreoContraseña() {

            when(usuarioRepositorio.obtenerCorreos()).thenReturn(Collections.emptyList());
            doNothing().when(validadorUsuario).validarCreacionUsuario(any(), any());
            when(passwordEncoder.encode(any())).thenReturn(user.getContraseña());
            when(usuarioRepositorio.guardar(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User resultado = crearUsuarioUseCase.ejecutarCreaciónUsuario(
                    user.getNombre(),
                    user.getCorreo(),
                    user.getContraseña(),
                    user.getTipoUsuario()
            );

            assertNotNull(resultado);
        }

    }

}
