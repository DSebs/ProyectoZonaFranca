package com.tayronadev.dominio.auditoria.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Entidad de dominio que representa un registro de auditoría para cambios
 * de estado en citas. Cada instancia registra QUIÉN (usuario) realizó
 * un cambio de estado en CUÁL cita (citaId) y CUÁNDO ocurrió.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class RegistroCambioEstado {
    
    @EqualsAndHashCode.Include
    private final String id;
    
    @NonNull
    private final String citaId;
    
    @NonNull
    private final String usuarioId;
    
    @NonNull
    private final String usuarioNombre;
    
    @NonNull
    private final TipoCambio tipoCambio;
    
    private final String estadoAnterior;
    
    @NonNull
    private final String estadoNuevo;
    
    private final String observaciones;
    
    @NonNull
    private final LocalDateTime fechaCambio;
    
    /**
     * Constructor para crear un nuevo registro de auditoría
     */
    public RegistroCambioEstado(@NonNull String citaId,
                                 @NonNull String usuarioId,
                                 @NonNull String usuarioNombre,
                                 @NonNull TipoCambio tipoCambio,
                                 String estadoAnterior,
                                 @NonNull String estadoNuevo,
                                 String observaciones) {
        this.id = UUID.randomUUID().toString();
        this.citaId = citaId;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.tipoCambio = tipoCambio;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.observaciones = observaciones;
        this.fechaCambio = LocalDateTime.now();
    }
    
    /**
     * Constructor para reconstruir un registro desde persistencia
     */
    public RegistroCambioEstado(@NonNull String id,
                                 @NonNull String citaId,
                                 @NonNull String usuarioId,
                                 @NonNull String usuarioNombre,
                                 @NonNull TipoCambio tipoCambio,
                                 String estadoAnterior,
                                 @NonNull String estadoNuevo,
                                 String observaciones,
                                 @NonNull LocalDateTime fechaCambio) {
        this.id = id;
        this.citaId = citaId;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.tipoCambio = tipoCambio;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.observaciones = observaciones;
        this.fechaCambio = fechaCambio;
    }
    
    /**
     * Obtiene las observaciones si existen
     */
    public Optional<String> getObservacionesOpt() {
        return Optional.ofNullable(observaciones);
    }
    
    /**
     * Obtiene el estado anterior si existe
     */
    public Optional<String> getEstadoAnteriorOpt() {
        return Optional.ofNullable(estadoAnterior);
    }
    
    /**
     * Verifica si el cambio fue un cambio de estado principal (no post-cita)
     */
    public boolean esCambioEstadoPrincipal() {
        return tipoCambio == TipoCambio.CONFIRMACION ||
               tipoCambio == TipoCambio.RECHAZO ||
               tipoCambio == TipoCambio.CANCELACION;
    }
    
    /**
     * Verifica si el cambio fue una asignación de estado post-cita
     */
    public boolean esCambioEstadoPostCita() {
        return tipoCambio == TipoCambio.ASIGNACION_ENTREGADO ||
               tipoCambio == TipoCambio.ASIGNACION_DEVUELTO ||
               tipoCambio == TipoCambio.ASIGNACION_TARDIA;
    }
}
