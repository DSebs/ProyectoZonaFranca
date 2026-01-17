package com.tayronadev.api.citas.dto.request;

import com.tayronadev.dominio.citas.modelo.OpcionTransporte.TipoTransporte;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la información de transporte.
 * Dependiendo del tipo, se validan diferentes campos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransporteRequest {
    
    @NotNull(message = "El tipo de transporte es obligatorio")
    private TipoTransporte tipoTransporte;
    
    // Campos para TRANSPORTADORA
    @Size(max = 200, message = "El nombre de la transportadora no puede exceder 200 caracteres")
    private String nombreTransportadora;
    
    @Size(max = 100, message = "El número de guía no puede exceder 100 caracteres")
    private String numeroGuia;
    
    // Campos para PARTICULAR
    @Size(max = 100, message = "El nombre del conductor no puede exceder 100 caracteres")
    private String conductorNombre;
    
    @Size(max = 20, message = "La cédula del conductor no puede exceder 20 caracteres")
    private String conductorCedula;
    
    @Size(max = 10, message = "La placa del vehículo no puede exceder 10 caracteres")
    private String placaVehiculo;
    
    // Auxiliar (opcional para ambos tipos)
    @Valid
    private AuxiliarRequest auxiliar;
    
    /**
     * Valida que los campos requeridos según el tipo de transporte estén presentes
     */
    public boolean esValido() {
        if (tipoTransporte == null) return false;
        
        return switch (tipoTransporte) {
            case TRANSPORTADORA -> nombreTransportadora != null && !nombreTransportadora.isBlank() 
                    && numeroGuia != null && !numeroGuia.isBlank();
            case PARTICULAR -> conductorNombre != null && !conductorNombre.isBlank()
                    && conductorCedula != null && !conductorCedula.isBlank()
                    && placaVehiculo != null && !placaVehiculo.isBlank();
        };
    }
}
