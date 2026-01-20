package com.tayronadev.dominio.usuario.repositorios;

import com.tayronadev.dominio.usuario.modelo.TipoUsuario;
import com.tayronadev.dominio.usuario.modelo.User;

import java.util.List;

public interface UsuarioRepositorio {


    //guardaremos
    User CrearUsuario(User user);

    void actualizarUsuario(String id, User user);

    void eliminarUsuario(String id);

    User obtenerPorCorreo(String correo);

    List<User> obtenerTodos();

    List<String> obtenerCorreos();

    List<String> obtenerNombre();

}
