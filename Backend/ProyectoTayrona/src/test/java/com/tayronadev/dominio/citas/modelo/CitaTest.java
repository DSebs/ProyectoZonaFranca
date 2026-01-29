package com.tayronadev.dominio.citas.modelo;

import com.tayronadev.dominio.citas.excepciones.EstadoCitaInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cita - Entidad de Dominio")
class CitaTest {
    
    private InformacionProveedor proveedor;
    private OpcionTransporte transporte;
    private Horario horario;
    
    @BeforeEach
    void setUp() {
        // Crear datos de prueba válidos
        var contacto = new DatosContacto("Juan Pérez", "juan@email.com", "3001234567");
        proveedor = new InformacionProveedor("Proveedor ABC", "900123456-1", "OC-001", contacto);
        transporte = new TransporteTransportadora("Servientrega", "GUIA-123");
        // Horario futuro en día laboral a las 10 AM
        horario = new Horario(obtenerProximoDiaLaboralA(10));
    }
    
    @Nested
    @DisplayName("Creación de Cita")
    class CreacionCita {
        
        @Test
        @DisplayName("Debe crear una cita con estado PENDIENTE")
        void debeCrearCitaConEstadoPendiente() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertEquals(EstadoCita.PENDIENTE, cita.getEstado());
            assertNotNull(cita.getId());
            assertNotNull(cita.getFechaCreacion());
        }
        
        @Test
        @DisplayName("Debe generar un ID único")
        void debeGenerarIdUnico() {
            var cita1 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            var cita2 = new Cita(TipoCita.RECOJO, proveedor, transporte, horario);
            
            assertNotEquals(cita1.getId(), cita2.getId());
        }
        
        @Test
        @DisplayName("No debe crear cita con horario fuera del horario laboral")
        void noDebeCrearCitaFueraDeHorarioLaboral() {
            var horarioNocturno = new Horario(obtenerProximoDiaLaboralA(22));
            
            assertThrows(IllegalArgumentException.class, 
                    () -> new Cita(TipoCita.ENTREGA, proveedor, transporte, horarioNocturno));
        }
        
        @Test
        @DisplayName("Debe inicializar sin observaciones ni estado post-cita")
        void debeInicializarSinObservacionesNiEstadoPost() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertTrue(cita.getObservaciones().isEmpty());
            assertTrue(cita.getEstadoPostCita().isEmpty());
        }
    }
    
    @Nested
    @DisplayName("Transiciones de Estado")
    class TransicionesEstado {
        
        @Test
        @DisplayName("Debe confirmar una cita pendiente")
        void debeConfirmarCitaPendiente() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            cita.confirmar("Confirmada sin observaciones");
            
            assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
            assertTrue(cita.getObservaciones().isPresent());
        }
        
        @Test
        @DisplayName("No debe confirmar una cita ya confirmada")
        void noDebeConfirmarCitaYaConfirmada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> cita.confirmar("Intento de re-confirmar"));
        }
        
        @Test
        @DisplayName("Debe rechazar una cita pendiente con motivo")
        void debeRechazarCitaPendienteConMotivo() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            cita.rechazar("Documentación incompleta");
            
            assertEquals(EstadoCita.RECHAZADA, cita.getEstado());
            assertEquals("Documentación incompleta", cita.getObservaciones().orElse(""));
        }
        
        @Test
        @DisplayName("No debe rechazar sin motivo")
        void noDebeRechazarSinMotivo() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertThrows(NullPointerException.class, () -> cita.rechazar(null));
        }
        
        @Test
        @DisplayName("Debe cancelar una cita pendiente")
        void debeCancelarCitaPendiente() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            cita.cancelar("Proveedor solicitó cancelación");
            
            assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        }
        
        @Test
        @DisplayName("Debe cancelar una cita confirmada")
        void debeCancelarCitaConfirmada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            cita.cancelar("Emergencia del proveedor");
            
            assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        }
        
        @Test
        @DisplayName("No debe cancelar una cita ya rechazada")
        void noDebeCancelarCitaRechazada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.rechazar("Motivo de rechazo");
            
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> cita.cancelar("Intento de cancelar rechazada"));
        }
        
        @Test
        @DisplayName("No debe cancelar sin motivo")
        void noDebeCancelarSinMotivo() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertThrows(NullPointerException.class, () -> cita.cancelar(null));
        }
    }
    
    @Nested
    @DisplayName("Estados Post-Cita")
    class EstadosPostCita {
        
        @Test
        @DisplayName("Debe marcar como entregada una cita confirmada")
        void debeMarcarComoEntregada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            cita.marcarComoEntregada();
            
            assertTrue(cita.getEstadoPostCita().isPresent());
            assertEquals(EstadoPostCita.ENTREGADO, cita.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("Debe marcar como devuelta una cita confirmada")
        void debeMarcarComoDevuelta() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            cita.marcarComoDevuelta();
            
            assertEquals(EstadoPostCita.DEVUELTO, cita.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("Debe marcar como tardía una cita confirmada")
        void debeMarcarComoTardia() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            cita.marcarComoTardia();
            
            assertEquals(EstadoPostCita.TARDIA, cita.getEstadoPostCita().get());
        }
        
        @Test
        @DisplayName("No debe asignar estado post-cita a cita pendiente")
        void noDebeAsignarEstadoPostACitaPendiente() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertThrows(EstadoCitaInvalidoException.class, cita::marcarComoEntregada);
        }
        
        @Test
        @DisplayName("No debe asignar estado post-cita a cita rechazada")
        void noDebeAsignarEstadoPostACitaRechazada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.rechazar("Rechazada");
            
            assertThrows(EstadoCitaInvalidoException.class, cita::marcarComoDevuelta);
        }
    }
    
    @Nested
    @DisplayName("Observaciones")
    class Observaciones {
        
        @Test
        @DisplayName("Debe agregar observaciones a cita pendiente")
        void debeAgregarObservacionesACitaPendiente() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            cita.agregarObservaciones("Requiere carga especial");
            
            assertEquals("Requiere carga especial", cita.getObservaciones().orElse(""));
        }
        
        @Test
        @DisplayName("No debe agregar observaciones a cita rechazada")
        void noDebeAgregarObservacionesACitaRechazada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.rechazar("Motivo");
            
            assertThrows(EstadoCitaInvalidoException.class, 
                    () -> cita.agregarObservaciones("Nuevas observaciones"));
        }
    }
    
    @Nested
    @DisplayName("Conflictos de Horario")
    class ConflictosHorario {
        
        @Test
        @DisplayName("Debe detectar conflicto con cita del mismo tipo y horario")
        void debeDetectarConflictoMismoTipoYHorario() {
            var cita1 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            var cita2 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertTrue(cita1.tieneConflictoDeHorarioCon(cita2));
        }
        
        @Test
        @DisplayName("No debe detectar conflicto con cita de diferente tipo")
        void noDebeDetectarConflictoDiferenteTipo() {
            var cita1 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            var horarioRecojo = new Horario(obtenerProximoDiaLaboralA(10));
            var cita2 = new Cita(TipoCita.RECOJO, proveedor, transporte, horarioRecojo);
            
            assertFalse(cita1.tieneConflictoDeHorarioCon(cita2));
        }
        
        @Test
        @DisplayName("No debe detectar conflicto con cita de diferente horario")
        void noDebeDetectarConflictoDiferenteHorario() {
            var cita1 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            var horarioDiferente = new Horario(obtenerProximoDiaLaboralA(14));
            var cita2 = new Cita(TipoCita.ENTREGA, proveedor, transporte, horarioDiferente);
            
            assertFalse(cita1.tieneConflictoDeHorarioCon(cita2));
        }
        
        @Test
        @DisplayName("No debe detectar conflicto con null")
        void noDebeDetectarConflictoConNull() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertFalse(cita.tieneConflictoDeHorarioCon(null));
        }
    }
    
    @Nested
    @DisplayName("Verificaciones de Estado")
    class VerificacionesEstado {
        
        @Test
        @DisplayName("Cita pendiente puede ser modificada")
        void citaPendientePuedeSerModificada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            
            assertTrue(cita.puedeSerModificada());
        }
        
        @Test
        @DisplayName("Cita confirmada puede ser modificada")
        void citaConfirmadaPuedeSerModificada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.confirmar(null);
            
            assertTrue(cita.puedeSerModificada());
        }
        
        @Test
        @DisplayName("Cita rechazada no puede ser modificada")
        void citaRechazadaNoPuedeSerModificada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.rechazar("Motivo");
            
            assertFalse(cita.puedeSerModificada());
        }
        
        @Test
        @DisplayName("Cita cancelada no puede ser modificada")
        void citaCanceladaNoPuedeSerModificada() {
            var cita = new Cita(TipoCita.ENTREGA, proveedor, transporte, horario);
            cita.cancelar("Motivo");
            
            assertFalse(cita.puedeSerModificada());
        }
    }
    
    // Método auxiliar para obtener el próximo día laboral a una hora específica
    private LocalDateTime obtenerProximoDiaLaboralA(int hora) {
        var ahora = LocalDateTime.now().plusDays(1).withHour(hora).withMinute(0).withSecond(0).withNano(0);
        
        // Avanzar hasta un día laboral (lunes a viernes)
        while (ahora.getDayOfWeek().getValue() > 5) {
            ahora = ahora.plusDays(1);
        }
        
        return ahora;
    }
}
