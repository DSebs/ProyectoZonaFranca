package com.tayronadev.api.usuario.controllers;

import com.tayronadev.api.usuario.dto.request.ActualizarUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.CrearUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.EliminarUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.IniciarSesionRequest;
import com.tayronadev.api.usuario.dto.response.ConfirmarResponse;
import com.tayronadev.api.usuario.dto.response.ListaUsuariosResponse;
import com.tayronadev.api.usuario.dto.response.LoginResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioResponse;
import com.tayronadev.api.usuario.mappers.UsuarioDtoMapper;
import com.tayronadev.dominio.usuario.casosuso.AutenticarUsuarioUseCase;
import com.tayronadev.dominio.usuario.casosuso.ConsultarUsuariosUseCase;
import com.tayronadev.dominio.usuario.casosuso.CrearUsuarioUseCase;
import com.tayronadev.dominio.usuario.casosuso.GestionarUsuarioUseCase;
import com.tayronadev.dominio.usuario.modelo.User;
import com.tayronadev.dominio.usuario.servicios.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final ConsultarUsuariosUseCase consultarUsuariosUseCase;
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final GestionarUsuarioUseCase gestionarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final Jwt jwt;
    private final UsuarioDtoMapper mapper;

    @PostMapping("/createUser")
    public ResponseEntity<UsuarioResponse> createUser(@Valid @RequestBody CrearUsuarioRequest request) {
        log.info("Creando nuevo usuario: {}", mapper.toCorreo(request));

        User user = crearUsuarioUseCase.ejecutar(
                mapper.toNombre(request),
                mapper.toCorreo(request),
                mapper.toContraseña(request),
                mapper.toTipoUsuario(request)
        );

        return ResponseEntity.ok(mapper.toUsuarioResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody IniciarSesionRequest request) {
        log.info("Intento de inicio de sesión para correo: {}", mapper.toCorreo(request));

        User user = autenticarUsuarioUseCase.ejecutar(
                mapper.toCorreo(request),
                mapper.toContraseña(request)
        );

        String token = jwt.generarToken(user);
        String refreshToken = jwt.generarReinicioToken(user);

        return ResponseEntity.ok(mapper.toLoginResponse(user, token, refreshToken));
    }

    @PostMapping("/updateUser")
    public ResponseEntity<ConfirmarResponse> updateUser(@Valid @RequestBody ActualizarUsuarioRequest request) {
        log.info("Actualizando datos de usuario: {}", mapper.toCorreo(request));

        User user = gestionarUsuarioUseCase.actualizarUsuario(
                mapper.toCorreo(request),
                mapper.toNuevaContraseña(request),
                mapper.toTipoUsuario(request),
                mapper.toCuentaActiva(request)
        );

        return ResponseEntity.ok(mapper.toConfirmarResponseActualizacion(user));
    }

    @GetMapping("/listUsers")
    public ResponseEntity<ListaUsuariosResponse> listUsers() {
        log.info("Obteniendo todos los usuarios");

        var usuarios = consultarUsuariosUseCase.obtenerTodos();
        return ResponseEntity.ok(mapper.toListaUsuariosResponse(usuarios));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ConfirmarResponse> deleteUser(@Valid @RequestBody EliminarUsuarioRequest request) {
        String correo = mapper.toCorreo(request);
        log.info("Eliminando usuario: {}", correo);

        gestionarUsuarioUseCase.eliminarUsuario(correo);

        return ResponseEntity.ok(mapper.toConfirmarResponse("Usuario eliminado correctamente"));
    }
}
