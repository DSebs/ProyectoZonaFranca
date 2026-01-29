package com.tayronadev.infraestructura.persistencia.entidades;

import com.tayronadev.dominio.citas.modelo.EstadoCita;
import com.tayronadev.dominio.citas.modelo.EstadoPostCita;
import com.tayronadev.dominio.citas.modelo.TipoCita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad JPA para persistir citas
 */
@Entity
@Table(name = "citas", indexes = {
    @Index(name = "idx_cita_estado", columnList = "estado"),
    @Index(name = "idx_cita_tipo", columnList = "tipo_cita"),
    @Index(name = "idx_cita_nit", columnList = "nit"),
    @Index(name = "idx_cita_fecha_hora", columnList = "fecha_hora"),
    @Index(name = "idx_cita_tipo_estado", columnList = "tipo_cita, estado"),
    @Index(name = "idx_cita_fecha_creacion", columnList = "fecha_creacion")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaEntity {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 20)
    private TipoCita tipoCita;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCita estado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_post_cita", length = 20)
    private EstadoPostCita estadoPostCita;
    
    // Información del proveedor
    @Column(name = "nombre_proveedor", nullable = false, length = 200)
    private String nombreProveedor;
    
    @Column(name = "nit", nullable = false, length = 50)
    private String nit;
    
    @Column(name = "numero_orden_compra", nullable = false, length = 100)
    private String numeroOrdenCompra;
    
    // Datos del responsable
    @Column(name = "responsable_nombre", nullable = false, length = 100)
    private String responsableNombre;
    
    @Column(name = "responsable_email", nullable = false, length = 100)
    private String responsableEmail;
    
    @Column(name = "responsable_telefono", nullable = false, length = 20)
    private String responsableTelefono;
    
    // Información de transporte
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transporte", nullable = false, length = 20)
    private TipoTransporteEntity tipoTransporte;
    
    // Campos para transportadora
    @Column(name = "nombre_transportadora", length = 200)
    private String nombreTransportadora;
    
    @Column(name = "numero_guia", length = 100)
    private String numeroGuia;
    
    // Campos para transporte particular
    @Column(name = "conductor_nombre", length = 100)
    private String conductorNombre;
    
    @Column(name = "conductor_cedula", length = 20)
    private String conductorCedula;
    
    @Column(name = "placa_vehiculo", length = 10)
    private String placaVehiculo;
    
    // Auxiliar de transporte (opcional)
    @Column(name = "auxiliar_nombre", length = 100)
    private String auxiliarNombre;
    
    @Column(name = "auxiliar_cedula", length = 20)
    private String auxiliarCedula;
    
    // Horario
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    // Observaciones
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    // Auditoría
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_ultima_modificacion", nullable = false)
    private LocalDateTime fechaUltimaModificacion;
    
    /**
     * Enum para el tipo de transporte en JPA
     */
    public enum TipoTransporteEntity {
        TRANSPORTADORA,
        PARTICULAR
    }
}