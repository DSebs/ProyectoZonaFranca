package com.tayronadev.infraestructura.persistencia.entidades;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tayronadev.dominio.usuario.modelo.TipoUsuario;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo_usuario", nullable = false, length = 100)
    private String correo;

    @Column(name = "contraseña_usuario", nullable = false, length = 100)
    private String contraseña;

    @Column(name = "estado_cuenta")
    private boolean cuentaActiva;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoUsuario tipoUsuario;
}
