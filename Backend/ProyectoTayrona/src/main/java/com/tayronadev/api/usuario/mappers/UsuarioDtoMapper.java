package com.tayronadev.api.usuario.mappers;

import com.tayronadev.api.usuario.dto.request.CrearUsuarioRequest;
import com.tayronadev.api.usuario.dto.response.ListaUsuariosResponse;
import com.tayronadev.api.usuario.dto.response.LoginResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioEliminadoResponse;
import com.tayronadev.api.usuario.dto.response.UsuarioResponse;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs de la API de usuario y objetos de dominio (User).
 * Sigue la misma lógica que {@link com.tayronadev.api.citas.mappers.CitaDtoMapper}.
 */
@Component
public class UsuarioDtoMapper {

    // ==================== Request -> parámetros para casos de uso ====================

    /**
     * Extrae nombre del request de creación de usuario.
     */
    public String toNombre(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getNombreUsuario();
    }

    /**
     * Extrae correo del request de creación de usuario.
     */
    public String toCorreo(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getCorreo();
    }

    /**
     * Extrae contraseña del request de creación de usuario.
     */
    public String toContraseña(CrearUsuarioRequest request) {
        return request.getInformacionUsuarioRequest().getContraseña();
    }

    /**
     * Extrae tipo de usuario del request de creación.
     */
    public TipoUsuario toTipoUsuario(CrearUsuarioRequest request) {
        return request.getTipoUsuario();
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
}
