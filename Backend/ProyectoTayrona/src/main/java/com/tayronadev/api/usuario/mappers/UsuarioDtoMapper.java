package com.tayronadev.api.usuario.mappers;

import com.tayronadev.api.usuario.dto.request.ActualizarUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.CrearUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.EliminarUsuarioRequest;
import com.tayronadev.api.usuario.dto.request.IniciarSesionRequest;
import com.tayronadev.api.usuario.dto.response.ConfirmarResponse;
import com.tayronadev.api.usuario.dto.response.ListaUsuariosResponse;
import com.tayronadev.api.usuario.dto.response.LoginResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioEliminadoResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioResponse;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioDtoMapper {

    // ==================== CrearUsuarioRequest ====================

    public String toNombre(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getNombreUsuario();
    }

    public String toCorreo(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getCorreo();
    }

    public String toContraseña(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getContraseña();
    }

    public TipoUsuario toTipoUsuario(CrearUsuarioRequest request) {
        return request.getTipoUsuario();
    }

    // ==================== IniciarSesionRequest ====================

    public String toCorreo(IniciarSesionRequest request) {
        return request.getCorreo();
    }

    public String toContraseña(IniciarSesionRequest request) {
        return request.getContraseña();
    }

    // ==================== ActualizarUsuarioRequest ====================

    public String toCorreo(ActualizarUsuarioRequest request) {
        return request.getCorreo();
    }

    public String toNuevaContraseña(ActualizarUsuarioRequest request) {
        return request.getNuevaContraseña();
    }

    public TipoUsuario toTipoUsuario(ActualizarUsuarioRequest request) {
        return request.getTipoUsuario();
    }

    public Boolean toCuentaActiva(ActualizarUsuarioRequest request) {
        return request.getCuentaActiva();
    }

    // ==================== EliminarUsuarioRequest ====================

    public String toCorreo(EliminarUsuarioRequest request) {
        return request.getCorreo();
    }

    // ==================== Dominio -> Response ====================

    /**
     * Convierte un User de dominio a UsuarioResponse.
     */
    public UsuarioResponse toUsuarioResponse(User user) {
        return UsuarioResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .correo(user.getCorreo())
                .tipoUsuario(user.getTipoUsuario())
                .cuentaActiva(user.isCuentaActiva())
                .build();
    }

    /**
     * Convierte una lista de User a lista de UsuarioResponse.
     */
    public List<UsuarioResponse> toUsuarioResponseList(List<User> usuarios) {
        return usuarios.stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de User a ListaUsuariosResponse.
     */
    public ListaUsuariosResponse toListaUsuariosResponse(List<User> usuarios) {
        return ListaUsuariosResponse.builder()
                .usuarios(toUsuarioResponseList(usuarios))
                .build();
    }

    /**
     * Construye LoginResponse a partir del User y los tokens generados.
     */
    public LoginResponse toLoginResponse(User user, String token, String refreshToken) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .usuarioId(user.getId())
                .rol(user.getTipoUsuario().getDescripcion())
                .build();
    }

    // ==================== ConfirmarResponse (mensajes de confirmación) ====================

    /**
     * Respuesta de confirmación con mensaje genérico (ej. eliminación).
     */
    public ConfirmarResponse toConfirmarResponse(String mensaje) {
        return ConfirmarResponse.builder()
                .mensaje(mensaje)
                .build();
    }

    /**
     * Respuesta de confirmación tras actualizar usuario.
     */
    public ConfirmarResponse toConfirmarResponseActualizacion(User user) {
        return ConfirmarResponse.builder()
                .mensaje("Usuario actualizado correctamente: " + user.getCorreo())
                .build();
    }
}
