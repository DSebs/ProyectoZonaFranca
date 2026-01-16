package com.tayronadev.dominio.citas.modelo;

public enum EstadoPostCita {

    ENTREGADO("El producto ha sido entregado"),
    DEVUELTO("El producto ha sido devuelto"),
    TARDIA("La cita ha sido tardia");


    private final String descripcion;

    EstadoPostCita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}