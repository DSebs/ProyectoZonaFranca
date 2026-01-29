package com.tayronadev.dominio.citas.servicios;

import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import com.tayronadev.dominio.citas.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GestorEstadosCita - Servicio de Dominio")
class GestorEstadosCitaTest {
    
    private GestorEstadosCita gestorEstados;
    
    @BeforeEach
    void setUp() {
        gestorEstados = new GestorEstadosCita();
    }
    
    @Nested
    @DisplayName("Transiciones válidas desde PENDIENTE")
    class TransicionesDesdePENDIENTE {
        
        @Test
        @DisplayName("PENDIENTE -> CONFIRMADA es válida")
        void pendienteAConfirmadaEsValida() {
            assertTrue(gestorEstados.esTransicionValida(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA));
        }
        
        @Test
        @DisplayName("PENDIENTE -> RECHAZADA es válida")
        void pendienteARechazadaEsValida() {
            assertTrue(gestorEstados.esTransicionValida(EstadoCita.PENDIENTE, EstadoCita.RECHAZADA));
        }
        
        @Test
        @DisplayName("PENDIENTE -> CANCELADA es válida")
        void pendienteACanceladaEsValida() {
            assertTrue(gestorEstados.esTransicionValida(EstadoCita.PENDIENTE, EstadoCita.CANCELADA));
        }
        
        @Test
        @DisplayName("PENDIENTE -> PENDIENTE no es válida")
        void pendienteAPendienteNoEsValida() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.PENDIENTE, EstadoCita.PENDIENTE));
        }
    }
    
    @Nested
    @DisplayName("Transiciones válidas desde CONFIRMADA")
    class TransicionesDesdeCONFIRMADA {
        
        @Test
        @DisplayName("CONFIRMADA -> CANCELADA es válida")
        void confirmadaACanceladaEsValida() {
            assertTrue(gestorEstados.esTransicionValida(EstadoCita.CONFIRMADA, EstadoCita.CANCELADA));
        }
        
        @Test
        @DisplayName("CONFIRMADA -> PENDIENTE no es válida")
        void confirmadaAPendienteNoEsValida() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CONFIRMADA, EstadoCita.PENDIENTE));
        }
        
        @Test
        @DisplayName("CONFIRMADA -> RECHAZADA no es válida")
        void confirmadaARechazadaNoEsValida() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CONFIRMADA, EstadoCita.RECHAZADA));
        }
        
        @Test
        @DisplayName("CONFIRMADA -> CONFIRMADA no es válida")
        void confirmadaAConfirmadaNoEsValida() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CONFIRMADA, EstadoCita.CONFIRMADA));
        }
    }
    
    @Nested
    @DisplayName("Transiciones desde estados finales")
    class TransicionesDesdeEstadosFinales {
        
        @Test
        @DisplayName("RECHAZADA no permite ninguna transición")
        void rechazadaNoPermiteTransiciones() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.RECHAZADA, EstadoCita.PENDIENTE));
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.RECHAZADA, EstadoCita.CONFIRMADA));
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.RECHAZADA, EstadoCita.CANCELADA));
        }
        
        @Test
        @DisplayName("CANCELADA no permite ninguna transición")
        void canceladaNoPermiteTransiciones() {
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CANCELADA, EstadoCita.PENDIENTE));
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CANCELADA, EstadoCita.CONFIRMADA));
            assertFalse(gestorEstados.esTransicionValida(EstadoCita.CANCELADA, EstadoCita.RECHAZADA));
        }
    }
    
    @Nested
    @DisplayName("Validación de transiciones con excepción")
    class ValidacionConExcepcion {
        
        @Test
        @DisplayName("Debe lanzar excepción para transición inválida")
        void debeLanzarExcepcionParaTransicionInvalida() {
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> gestorEstados.validarTransicion(EstadoCita.RECHAZADA, EstadoCita.CONFIRMADA, "confirmar"));
        }
        
        @Test
        @DisplayName("No debe lanzar excepción para transición válida")
        void noDebeLanzarExcepcionParaTransicionValida() {
            assertDoesNotThrow(
                    () -> gestorEstados.validarTransicion(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, "confirmar"));
        }
    }
    
    @Nested
    @DisplayName("Verificación de permisos por rol")
    class VerificacionPermisos {
        
        private Cita crearCitaConEstado(EstadoCita estado) {
            var contacto = new DatosContacto("Juan", "juan@email.com", "123");
            var proveedor = new InformacionProveedor("Proveedor", "123", "OC-1", contacto);
            var transporte = new TransporteTransportadora("Trans", "GUIA-1");
            var horario = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
            
            return new Cita("test-id", TipoCita.ENTREGA, proveedor, transporte, horario,
                    estado, null, null, LocalDateTime.now(), LocalDateTime.now());
        }
        
        @Test
        @DisplayName("Administrador puede modificar cita PENDIENTE")
        void adminPuedeModificarCitaPendiente() {
            var cita = crearCitaConEstado(EstadoCita.PENDIENTE);
            
            assertTrue(gestorEstados.puedeSerModificadaPorAdministrador(cita));
        }
        
        @Test
        @DisplayName("Administrador puede modificar cita CONFIRMADA")
        void adminPuedeModificarCitaConfirmada() {
            var cita = crearCitaConEstado(EstadoCita.CONFIRMADA);
            
            assertTrue(gestorEstados.puedeSerModificadaPorAdministrador(cita));
        }
        
        @Test
        @DisplayName("Administrador NO puede modificar cita RECHAZADA")
        void adminNoPuedeModificarCitaRechazada() {
            var cita = crearCitaConEstado(EstadoCita.RECHAZADA);
            
            assertFalse(gestorEstados.puedeSerModificadaPorAdministrador(cita));
        }
        
        @Test
        @DisplayName("Administrador NO puede modificar cita CANCELADA")
        void adminNoPuedeModificarCitaCancelada() {
            var cita = crearCitaConEstado(EstadoCita.CANCELADA);
            
            assertFalse(gestorEstados.puedeSerModificadaPorAdministrador(cita));
        }
        
        @Test
        @DisplayName("Proveedor puede cancelar cita PENDIENTE")
        void proveedorPuedeCancelarCitaPendiente() {
            var cita = crearCitaConEstado(EstadoCita.PENDIENTE);
            
            assertTrue(gestorEstados.puedeSerCanceladaPorProveedor(cita));
        }
        
        @Test
        @DisplayName("Proveedor puede cancelar cita CONFIRMADA")
        void proveedorPuedeCancelarCitaConfirmada() {
            var cita = crearCitaConEstado(EstadoCita.CONFIRMADA);
            
            assertTrue(gestorEstados.puedeSerCanceladaPorProveedor(cita));
        }
        
        @Test
        @DisplayName("Proveedor NO puede cancelar cita RECHAZADA")
        void proveedorNoPuedeCancelarCitaRechazada() {
            var cita = crearCitaConEstado(EstadoCita.RECHAZADA);
            
            assertFalse(gestorEstados.puedeSerCanceladaPorProveedor(cita));
        }
        
        @Test
        @DisplayName("Proveedor NO puede cancelar cita CANCELADA")
        void proveedorNoPuedeCancelarCitaCancelada() {
            var cita = crearCitaConEstado(EstadoCita.CANCELADA);
            
            assertFalse(gestorEstados.puedeSerCanceladaPorProveedor(cita));
        }
    }
}
