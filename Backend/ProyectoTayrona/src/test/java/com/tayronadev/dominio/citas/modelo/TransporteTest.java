package com.tayronadev.dominio.citas.modelo;

import com.tayronadev.dominio.citas.modelo.OpcionTransporte.DatosAuxiliar;
import com.tayronadev.dominio.citas.modelo.OpcionTransporte.TipoTransporte;
import com.tayronadev.dominio.citas.modelo.TransporteParticular.DatosConductor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Opciones de Transporte - Value Objects")
class TransporteTest {
    
    @Nested
    @DisplayName("TransporteTransportadora")
    class TransporteTransportadoraTest {
        
        @Test
        @DisplayName("Debe crear transporte de transportadora válido")
        void debeCrearTransporteTransportadoraValido() {
            var transporte = new TransporteTransportadora("Servientrega", "GUIA-123");
            
            assertEquals("Servientrega", transporte.getNombreTransportadora());
            assertEquals("GUIA-123", transporte.getNumeroGuia());
            assertEquals(TipoTransporte.TRANSPORTADORA, transporte.getTipo());
            assertTrue(transporte.getAuxiliar().isEmpty());
        }
        
        @Test
        @DisplayName("Debe crear transporte con auxiliar")
        void debeCrearTransporteConAuxiliar() {
            var auxiliar = new DatosAuxiliar("Auxiliar", "12345");
            var transporte = new TransporteTransportadora("Servientrega", "GUIA-123", auxiliar);
            
            assertTrue(transporte.getAuxiliar().isPresent());
            assertEquals("Auxiliar", transporte.getAuxiliar().get().getNombre());
        }
        
        @Test
        @DisplayName("No debe aceptar nombre de transportadora vacío")
        void noDebeAceptarNombreTransportadoraVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new TransporteTransportadora("", "GUIA-123"));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre de transportadora con solo espacios")
        void noDebeAceptarNombreTransportadoraConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new TransporteTransportadora("   ", "GUIA-123"));
        }
        
        @Test
        @DisplayName("No debe aceptar número de guía vacío")
        void noDebeAceptarNumeroGuiaVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new TransporteTransportadora("Servientrega", ""));
        }
        
        @Test
        @DisplayName("No debe aceptar número de guía con solo espacios")
        void noDebeAceptarNumeroGuiaConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new TransporteTransportadora("Servientrega", "   "));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre de transportadora null")
        void noDebeAceptarNombreTransportadoraNull() {
            assertThrows(NullPointerException.class, 
                    () -> new TransporteTransportadora(null, "GUIA-123"));
        }
        
        @Test
        @DisplayName("No debe aceptar número de guía null")
        void noDebeAceptarNumeroGuiaNull() {
            assertThrows(NullPointerException.class, 
                    () -> new TransporteTransportadora("Servientrega", null));
        }
    }
    
    @Nested
    @DisplayName("TransporteParticular")
    class TransporteParticularTest {
        
        @Test
        @DisplayName("Debe crear transporte particular válido")
        void debeCrearTransporteParticularValido() {
            var conductor = new DatosConductor("Juan", "12345", "ABC-123");
            var transporte = new TransporteParticular(conductor);
            
            assertEquals(conductor, transporte.getConductor());
            assertEquals(TipoTransporte.PARTICULAR, transporte.getTipo());
            assertTrue(transporte.getAuxiliar().isEmpty());
        }
        
        @Test
        @DisplayName("Debe crear transporte particular con auxiliar")
        void debeCrearTransporteParticularConAuxiliar() {
            var conductor = new DatosConductor("Juan", "12345", "ABC-123");
            var auxiliar = new DatosAuxiliar("Auxiliar", "67890");
            var transporte = new TransporteParticular(conductor, auxiliar);
            
            assertTrue(transporte.getAuxiliar().isPresent());
            assertEquals("Auxiliar", transporte.getAuxiliar().get().getNombre());
        }
        
        @Test
        @DisplayName("No debe aceptar conductor null")
        void noDebeAceptarConductorNull() {
            assertThrows(NullPointerException.class, 
                    () -> new TransporteParticular(null));
        }
    }
    
    @Nested
    @DisplayName("DatosConductor")
    class DatosConductorTest {
        
        @Test
        @DisplayName("Debe crear datos de conductor válidos")
        void debeCrearDatosConductorValidos() {
            var conductor = new DatosConductor("Juan Pérez", "12345678", "ABC-123");
            
            assertEquals("Juan Pérez", conductor.getNombre());
            assertEquals("12345678", conductor.getCedula());
            assertEquals("ABC-123", conductor.getPlacaVehiculo());
        }
        
        @Test
        @DisplayName("No debe aceptar nombre vacío")
        void noDebeAceptarNombreVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosConductor("", "12345", "ABC-123"));
        }
        
        @Test
        @DisplayName("No debe aceptar cédula vacía")
        void noDebeAceptarCedulaVacia() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosConductor("Juan", "", "ABC-123"));
        }
        
        @Test
        @DisplayName("No debe aceptar placa vacía")
        void noDebeAceptarPlacaVacia() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosConductor("Juan", "12345", ""));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre null")
        void noDebeAceptarNombreNull() {
            assertThrows(NullPointerException.class, 
                    () -> new DatosConductor(null, "12345", "ABC-123"));
        }
    }
    
    @Nested
    @DisplayName("DatosAuxiliar")
    class DatosAuxiliarTest {
        
        @Test
        @DisplayName("Debe crear datos de auxiliar válidos")
        void debeCrearDatosAuxiliarValidos() {
            var auxiliar = new DatosAuxiliar("Pedro López", "98765432");
            
            assertEquals("Pedro López", auxiliar.getNombre());
            assertEquals("98765432", auxiliar.getCedula());
        }
        
        @Test
        @DisplayName("No debe aceptar nombre vacío")
        void noDebeAceptarNombreVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosAuxiliar("", "12345"));
        }
        
        @Test
        @DisplayName("No debe aceptar cédula vacía")
        void noDebeAceptarCedulaVacia() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosAuxiliar("Pedro", ""));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre con solo espacios")
        void noDebeAceptarNombreConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosAuxiliar("   ", "12345"));
        }
        
        @Test
        @DisplayName("No debe aceptar cédula con solo espacios")
        void noDebeAceptarCedulaConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosAuxiliar("Pedro", "   "));
        }
    }
}
