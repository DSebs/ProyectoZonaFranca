package com.tayronadev.dominio.citas.modelo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;

/**
 * Entidad de dominio que representa una Cita en el sistema de agendamiento.
 * Esta es la entidad raíz del agregado Cita.
 */
public class Cita {
    
    private final String id;
    private final TipoCita tipoCita;
    private final InformacionProveedor proveedor;
    private final OpcionTransporte transporte;
    private final Horario horario;
    private final LocalDateTime fechaCreacion;
    
    private EstadoCita estado;
    private String observaciones;
    private EstadoPostCita estadoPostCita;
    private LocalDateTime fechaUltimaModificacion;
    
    /**
     * Constructor para crear una nueva cita
     */
    public Cita(TipoCita tipoCita, 
                InformacionProveedor proveedor, 
                OpcionTransporte transporte, 
                Horario horario) {
        this.id = UUID.randomUUID().toString();
        this.tipoCita = Objects.requireNonNull(tipoCita, "El tipo de cita es obligatorio");
        this.proveedor = Objects.requireNonNull(proveedor, "La información del proveedor es obligatoria");
        this.transporte = Objects.requireNonNull(transporte, "La información de transporte es obligatoria");
        this.horario = Objects.requireNonNull(horario, "El horario es obligatorio");
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
    public Cita(String id,
                TipoCita tipoCita,
                InformacionProveedor proveedor,
                OpcionTransporte transporte,
                Horario horario,
                EstadoCita estado,
                String observaciones,
                LocalDateTime fechaCreacion,
                LocalDateTime fechaUltimaModificacion) {
        this.id = Objects.requireNonNull(id, "El ID es obligatorio");
        this.tipoCita = Objects.requireNonNull(tipoCita, "El tipo de cita es obligatorio");
        this.proveedor = Objects.requireNonNull(proveedor, "La información del proveedor es obligatoria");
        this.transporte = Objects.requireNonNull(transporte, "La información de transporte es obligatoria");
        this.horario = Objects.requireNonNull(horario, "El horario es obligatorio");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.observaciones = observaciones;
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación es obligatoria");
        this.fechaUltimaModificacion = Objects.requireNonNull(fechaUltimaModificacion, "La fecha de última modificación es obligatoria");
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
    
    // Getters
    public String getId() { return id; }
    public TipoCita getTipoCita() { return tipoCita; }
    public InformacionProveedor getProveedor() { return proveedor; }
    public OpcionTransporte getTransporte() { return transporte; }
    public Horario getHorario() { return horario; }
    public EstadoCita getEstado() { return estado; }
    public Optional<String> getObservaciones() { return Optional.ofNullable(observaciones); }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaUltimaModificacion() { return fechaUltimaModificacion; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cita cita = (Cita) o;
        return Objects.equals(id, cita.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Cita{id='%s', tipo=%s, estado=%s, proveedor='%s', horario=%s}", 
                           id, tipoCita, estado, proveedor.nombreProveedor(), horario.fechaHora());
    }
}