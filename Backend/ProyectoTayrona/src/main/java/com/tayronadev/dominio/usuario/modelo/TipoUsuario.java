package com.tayronadev.dominio.usuario.modelo;

public enum TipoUsuario {
    ADMINISTRADOR("Administrador"),
    VISUALIZADOR("Visualizador");

    private final String descripcion;

    TipoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}