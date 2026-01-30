package com.tayronadev.api.usuario.controllers;

import com.tayronadev.api.usuario.dto.request.CrearUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.IniciarSesionRequest;
import com.tayronadev.api.usuario.dto.response.LoginResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioResponse;
import com.tayronadev.api.usuario.mappers.UsuarioDtoMapper;
import com.tayronadev.dominio.usuario.casosuso.AutenticarUsuarioUseCase;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.servicios.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final Jwt jwt;
    private final UsuarioDtoMapper mapper;

    @PostMapping("/cretaeUser")
    public ResponseEntity<UsuarioResponse> sinIn(@Valid @RequestBody CrearUsuarioRequest request) {



    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@Valid @RequestBody IniciarSesionRequest request) {
        log.info("Intento de inicio de sesión para correo: {}", request.getCorreo());

        User user = autenticarUsuarioUseCase.ejecutar(request.getCorreo(), request.getContraseña());

        String token = jwt.generarToken(user);
        String refreshToken = jwt.generarReinicioToken(user);

        LoginResponse response = mapper.toLoginResponse(user, token, refreshToken);

        log.info("Login exitoso para usuario: {} ({})", user.getNombre(), user.getCorreo());
        return ResponseEntity.ok(response);
    }
}
