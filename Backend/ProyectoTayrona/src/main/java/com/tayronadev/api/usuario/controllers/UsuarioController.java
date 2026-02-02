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

    @PostMapping("/cretaeUser")
    public ResponseEntity<UsuarioResponse> sinIn(@Valid @RequestBody CrearUsuarioRequest request) {
         log.info("Creando nuevo usuario: {}", request.getInformacionUsuarioRequest());

            User user = crearUsuarioUseCase.ejecutar(
                    request.getInformacionUsuarioRequest().getNombreUsuario(),
                    request.getInformacionUsuarioRequest().getCorreo(),
                    request.getInformacionUsuarioRequest().getContraseña(),
                    request.getTipoUsuario());

            UsuarioResponse response = mapper.toUsuarioResponse(user);
            log.info("Login exitoso para usuario: {} ({})", user.getNombre(), user.getCorreo());
            return ResponseEntity.ok(response);
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

    @PostMapping("/updateUser")
    public ResponseEntity<ConfirmarResponse> updateUser(@Valid @RequestBody ActualizarUsuarioRequest request){
        log.info("Actualizando datos de usuario: {}", request.getCorreo());

        User user = gestionarUsuarioUseCase.actualizarUsuario(
                request.getCorreo(),
                request.getNuevaContraseña(),
                request.getTipoUsuario(),
                request.getCuentaActiva());

        return ResponseEntity.ok(ConfirmarResponse.builder()
                .mensaje("Usuario actualizado correctamente:" + " " + user.getCorreo())
                .build());

    }


    @GetMapping("/ListUsers")
    public ResponseEntity<ListaUsuariosResponse> listUser(){
        log.info("Obteniendo todos los usuarios");

        var usuarios = consultarUsuariosUseCase.obtenerTodos();

        ListaUsuariosResponse listaUsuariosResponse = mapper.toListaUsuariosResponse(usuarios);

        return ResponseEntity.ok(listaUsuariosResponse);

    }

    @DeleteMapping("/DeleteUser")
    public ResponseEntity<ConfirmarResponse> eliminarUsuario(@RequestBody EliminarUsuarioRequest eliminarUsuarioRequest){

        gestionarUsuarioUseCase.eliminarUsuario(eliminarUsuarioRequest.getCorreo());

        return ResponseEntity.ok(ConfirmarResponse.builder()
                .mensaje("Usuario eliminado correctamente")
                .build());

    }



}
