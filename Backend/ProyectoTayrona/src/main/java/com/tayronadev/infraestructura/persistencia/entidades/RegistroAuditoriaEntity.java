package com.tayronadev.infraestructura.persistencia.entidades;

import com.tayronadev.dominio.auditoria.modelo.TipoCambio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad JPA para persistir registros de auditoría de cambios de estado en citas.
 */
@Entity
@Table(name = "auditoria_cambios_estado", indexes = {
    @Index(name = "idx_auditoria_cita_id", columnList = "cita_id"),
    @Index(name = "idx_auditoria_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_auditoria_tipo_cambio", columnList = "tipo_cambio"),
    @Index(name = "idx_auditoria_fecha_cambio", columnList = "fecha_cambio")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAuditoriaEntity {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "cita_id", nullable = false, length = 36)
    private String citaId;
    
    @Column(name = "usuario_id", nullable = false, length = 36)
    private String usuarioId;
    
    @Column(name = "usuario_nombre", nullable = false, length = 100)
    private String usuarioNombre;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cambio", nullable = false, length = 30)
    private TipoCambio tipoCambio;
    
    @Column(name = "estado_anterior", length = 20)
    private String estadoAnterior;
    
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private String estadoNuevo;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @CreationTimestamp
    @Column(name = "fecha_cambio", nullable = false, updatable = false)
    private LocalDateTime fechaCambio;
}
