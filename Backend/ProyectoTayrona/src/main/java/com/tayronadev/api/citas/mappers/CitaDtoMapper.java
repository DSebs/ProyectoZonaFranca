package com.tayronadev.api.citas.mappers;

import com.tayronadev.api.citas.dto.request.CrearCitaRequest;
import com.tayronadev.api.citas.dto.request.TransporteRequest;
import com.tayronadev.api.citas.dto.response.*;
import com.tayronadev.dominio.citas.modelo.*;
import com.tayronadev.dominio.citas.servicios.GestorEstadosCita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs de la API y objetos de dominio
 */
@Component
@RequiredArgsConstructor
public class CitaDtoMapper {
    
    private final GestorEstadosCita gestorEstados;
    
    // ==================== Request -> Dominio ====================
    
    /**
     * Convierte los datos del request en objetos de dominio para crear una cita
     */
    public InformacionProveedor toInformacionProveedor(CrearCitaRequest request) {
        var proveedorReq = request.getProveedor();
        var responsableReq = proveedorReq.getResponsable();
        
        var datosContacto = new DatosContacto(
                responsableReq.getNombre(),
                responsableReq.getEmail(),
                responsableReq.getTelefono()
        );
        
        return new InformacionProveedor(
                proveedorReq.getNombreProveedor(),
                proveedorReq.getNit(),
                proveedorReq.getNumeroOrdenCompra(),
                datosContacto
        );
    }
    
    /**
     * Convierte el request de transporte al objeto de dominio correspondiente
     */
    public OpcionTransporte toOpcionTransporte(TransporteRequest request) {
        OpcionTransporte.DatosAuxiliar auxiliar = null;
        
        if (request.getAuxiliar() != null) {
            auxiliar = new OpcionTransporte.DatosAuxiliar(
                    request.getAuxiliar().getNombre(),
                    request.getAuxiliar().getCedula()
            );
        }
        
        return switch (request.getTipoTransporte()) {
            case TRANSPORTADORA -> new TransporteTransportadora(
                    request.getNombreTransportadora(),
                    request.getNumeroGuia(),
                    auxiliar
            );
            case PARTICULAR -> {
                var conductor = new TransporteParticular.DatosConductor(
                        request.getConductorNombre(),
                        request.getConductorCedula(),
                        request.getPlacaVehiculo()
                );
                yield new TransporteParticular(conductor, auxiliar);
            }
        };
    }
    
    /**
     * Crea un Horario desde el request
     */
    public Horario toHorario(CrearCitaRequest request) {
        return new Horario(request.getFechaHora());
    }
    
    // ==================== Dominio -> Response ====================
    
    /**
     * Convierte una Cita de dominio a CitaResponse completo
     */
    public CitaResponse toCitaResponse(Cita cita) {
        return CitaResponse.builder()
                .id(cita.getId())
                .tipoCita(cita.getTipoCita().name())
                .tipoCitaDescripcion(cita.getTipoCita().getDescripcion())
                .estado(cita.getEstado().name())
                .estadoDescripcion(cita.getEstado().getDescripcion())
                .estadoPostCita(cita.getEstadoPostCita().map(Enum::name).orElse(null))
                .estadoPostCitaDescripcion(cita.getEstadoPostCita()
                        .map(EstadoPostCita::getDescripcion).orElse(null))
                .proveedor(toProveedorResponse(cita.getProveedor()))
                .transporte(toTransporteResponse(cita.getTransporte()))
                .fechaHora(cita.getHorario().getFechaHora())
                .observaciones(cita.getObservaciones().orElse(null))
                .fechaCreacion(cita.getFechaCreacion())
                .fechaUltimaModificacion(cita.getFechaUltimaModificacion())
                .puedeSerModificada(gestorEstados.puedeSerModificadaPorAdministrador(cita))
                .puedeSerCancelada(gestorEstados.puedeSerCanceladaPorProveedor(cita))
                .build();
    }
    
    /**
     * Convierte una Cita de dominio a CitaResumenResponse
     */
    public CitaResumenResponse toCitaResumenResponse(Cita cita) {
        return CitaResumenResponse.builder()
                .id(cita.getId())
                .tipoCita(cita.getTipoCita().name())
                .tipoCitaDescripcion(cita.getTipoCita().getDescripcion())
                .estado(cita.getEstado().name())
                .estadoDescripcion(cita.getEstado().getDescripcion())
                .estadoPostCita(cita.getEstadoPostCita().map(Enum::name).orElse(null))
                .nombreProveedor(cita.getProveedor().getNombreProveedor())
                .nit(cita.getProveedor().getNit())
                .fechaHora(cita.getHorario().getFechaHora())
                .fechaCreacion(cita.getFechaCreacion())
                .build();
    }
    
    /**
     * Convierte una lista de citas a lista de resúmenes
     */
    public List<CitaResumenResponse> toCitaResumenResponseList(List<Cita> citas) {
        return citas.stream()
                .map(this::toCitaResumenResponse)
                .collect(Collectors.toList());
    }
    
    private InformacionProveedorResponse toProveedorResponse(InformacionProveedor proveedor) {
        return InformacionProveedorResponse.builder()
                .nombreProveedor(proveedor.getNombreProveedor())
                .nit(proveedor.getNit())
                .numeroOrdenCompra(proveedor.getNumeroOrdenCompra())
                .responsable(DatosContactoResponse.builder()
                        .nombre(proveedor.getResponsable().getNombre())
                        .email(proveedor.getResponsable().getEmail())
                        .telefono(proveedor.getResponsable().getTelefono())
                        .build())
                .build();
    }
    
    private TransporteResponse toTransporteResponse(OpcionTransporte transporte) {
        var builder = TransporteResponse.builder()
                .tipoTransporte(transporte.getTipo().name());
        
        // Agregar datos del auxiliar si existe
        transporte.getAuxiliar().ifPresent(aux -> {
            builder.auxiliarNombre(aux.getNombre());
            builder.auxiliarCedula(aux.getCedula());
        });
        
        if (transporte instanceof TransporteTransportadora transportadora) {
            builder.nombreTransportadora(transportadora.getNombreTransportadora());
            builder.numeroGuia(transportadora.getNumeroGuia());
        } else if (transporte instanceof TransporteParticular particular) {
            var conductor = particular.getConductor();
            builder.conductorNombre(conductor.getNombre());
            builder.conductorCedula(conductor.getCedula());
            builder.placaVehiculo(conductor.getPlacaVehiculo());
        }
        
        return builder.build();
    }
}
