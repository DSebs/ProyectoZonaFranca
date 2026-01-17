package com.tayronadev.dominio.citas.modelo;

import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Entidad de dominio que representa una Cita en el sistema de agendamiento.
 * Esta es la entidad raíz del agregado Cita.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"observaciones", "estadoPostCita"})
public class Cita {
    
    @EqualsAndHashCode.Include
    private final String id;
    @NonNull
    private final TipoCita tipoCita;
    @NonNull
    private final InformacionProveedor proveedor;
    @NonNull
    private final OpcionTransporte transporte;
    @NonNull
    private final Horario horario;
    private final LocalDateTime fechaCreacion;
    
    private EstadoCita estado;
    private String observaciones;
    private EstadoPostCita estadoPostCita;
    private LocalDateTime fechaUltimaModificacion;
    
    /**
     * Constructor para crear una nueva cita
     */
    public Cita(@NonNull TipoCita tipoCita, 
                @NonNull InformacionProveedor proveedor, 
                @NonNull OpcionTransporte transporte, 
                @NonNull Horario horario) {
        this.id = UUID.randomUUID().toString();
        this.tipoCita = tipoCita;
        this.proveedor = proveedor;
        this.transporte = transporte;
        this.horario = horario;
        this.estado = EstadoCita.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimaModificacion = LocalDateTime.now();
        
        // Validaciones de negocio
        validarHorario();
        transporte.validar();
    }
    
    /**
     * Constructor para reconstruir una cita existente (desde persistencia)
     */
    public Cita(@NonNull String id,
                @NonNull TipoCita tipoCita,
                @NonNull InformacionProveedor proveedor,
                @NonNull OpcionTransporte transporte,
                @NonNull Horario horario,
                @NonNull EstadoCita estado,
                String observaciones,
                EstadoPostCita estadoPostCita,
                @NonNull LocalDateTime fechaCreacion,
                @NonNull LocalDateTime fechaUltimaModificacion) {
        this.id = id;
        this.tipoCita = tipoCita;
        this.proveedor = proveedor;
        this.transporte = transporte;
        this.horario = horario;
        this.estado = estado;
        this.observaciones = observaciones;
        this.estadoPostCita = estadoPostCita;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }
    
    /**
     * Confirma la cita si está en estado pendiente
     */
    public void confirmar(String observaciones) {
        if (estado != EstadoCita.PENDIENTE) {
            throw new EstadoCitaInvalidoException(estado, "confirmar");
        }
        this.estado = EstadoCita.CONFIRMADA;
        this.observaciones = observaciones;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Rechaza la cita si está en estado pendiente
     */
    public void rechazar(String motivoRechazo) {
        if (estado != EstadoCita.PENDIENTE) {
            throw new EstadoCitaInvalidoException(estado, "rechazar");
        }
        Objects.requireNonNull(motivoRechazo, "El motivo de rechazo es obligatorio");
        
        this.estado = EstadoCita.RECHAZADA;
        this.observaciones = motivoRechazo;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Cancela la cita si está en estado pendiente o confirmada
     */
    public void cancelar(String motivoCancelacion) {
        if (estado.esFinal()) {
            throw new EstadoCitaInvalidoException(estado, "cancelar");
        }
        Objects.requireNonNull(motivoCancelacion, "El motivo de cancelación es obligatorio");
        
        this.estado = EstadoCita.CANCELADA;
        this.observaciones = motivoCancelacion;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Agrega observaciones a la cita
     */
    public void agregarObservaciones(String nuevasObservaciones) {
        if (!estado.permiteModificacion()) {
            throw new EstadoCitaInvalidoException(estado, "agregar observaciones");
        }
        this.observaciones = nuevasObservaciones;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Marca la cita como entregada (solo si está confirmada)
     */
    public void marcarComoEntregada() {
        validarEstadoParaPostCita();
        this.estadoPostCita = EstadoPostCita.ENTREGADO;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Marca la cita como devuelta (solo si está confirmada)
     */
    public void marcarComoDevuelta() {
        validarEstadoParaPostCita();
        this.estadoPostCita = EstadoPostCita.DEVUELTO;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Marca la cita como tardía (solo si está confirmada)
     */
    public void marcarComoTardia() {
        validarEstadoParaPostCita();
        this.estadoPostCita = EstadoPostCita.TARDIA;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Verifica si la cita tiene un estado post-cita definido
     */
    public boolean tieneEstadoPostCita() {
        return estadoPostCita != null;
    }
    
    private void validarEstadoParaPostCita() {
        if (estado != EstadoCita.CONFIRMADA) {
            throw new EstadoCitaInvalidoException(estado, "establecer estado post-cita");
        }
    }
    
    /**
     * Verifica si la cita puede ser modificada
     */
    public boolean puedeSerModificada() {
        return estado.permiteModificacion();
    }
    
    /**
     * Verifica si hay conflicto de horario con otra cita del mismo tipo
     */
    public boolean tieneConflictoDeHorarioCon(Cita otraCita) {
        if (otraCita == null || !this.tipoCita.equals(otraCita.tipoCita)) {
            return false;
        }
        return this.horario.conflictoCon(otraCita.horario);
    }
    
    private void validarHorario() {
        if (!horario.esDentroDeHorarioLaboral()) {
            throw new IllegalArgumentException("El horario debe estar dentro del horario laboral");
        }
    }
    
    // Métodos adicionales para Optional wrapping (Lombok no los genera automáticamente)
    public Optional<String> getObservaciones() { 
        return Optional.ofNullable(observaciones); 
    }
    
    public Optional<EstadoPostCita> getEstadoPostCita() { 
        return Optional.ofNullable(estadoPostCita); 
    }
}