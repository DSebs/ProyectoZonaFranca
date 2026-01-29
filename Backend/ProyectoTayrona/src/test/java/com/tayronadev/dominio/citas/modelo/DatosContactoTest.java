package com.tayronadev.dominio.citas.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DatosContacto - Value Object")
class DatosContactoTest {
    
    @Nested
    @DisplayName("Creación válida")
    class CreacionValida {
        
        @Test
        @DisplayName("Debe crear datos de contacto válidos")
        void debeCrearDatosContactoValidos() {
            var contacto = new DatosContacto("Juan Pérez", "juan@email.com", "3001234567");
            
            assertEquals("Juan Pérez", contacto.getNombre());
            assertEquals("juan@email.com", contacto.getEmail());
            assertEquals("3001234567", contacto.getTelefono());
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"test@domain.com", "user.name@company.co", "a@b.c"})
        @DisplayName("Debe aceptar emails válidos")
        void debeAceptarEmailsValidos(String email) {
            assertDoesNotThrow(() -> new DatosContacto("Nombre", email, "123456"));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de nombre")
    class ValidacionesNombre {
        
        @Test
        @DisplayName("No debe aceptar nombre vacío")
        void noDebeAceptarNombreVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("", "email@test.com", "123456"));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre con solo espacios")
        void noDebeAceptarNombreConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("   ", "email@test.com", "123456"));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre null")
        void noDebeAceptarNombreNull() {
            assertThrows(NullPointerException.class, 
                    () -> new DatosContacto(null, "email@test.com", "123456"));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de email")
    class ValidacionesEmail {
        
        @Test
        @DisplayName("No debe aceptar email sin arroba")
        void noDebeAceptarEmailSinArroba() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("Nombre", "emailsinarro.com", "123456"));
        }
        
        @Test
        @DisplayName("No debe aceptar email sin punto")
        void noDebeAceptarEmailSinPunto() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("Nombre", "email@dominio", "123456"));
        }
        
        @Test
        @DisplayName("No debe aceptar email null")
        void noDebeAceptarEmailNull() {
            assertThrows(NullPointerException.class, 
                    () -> new DatosContacto("Nombre", null, "123456"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"invalido", "usuario@dominio"})
        @DisplayName("No debe aceptar emails sin @ o sin punto")
        void noDebeAceptarEmailsSinArrobaOSinPunto(String email) {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("Nombre", email, "123456"));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de teléfono")
    class ValidacionesTelefono {
        
        @Test
        @DisplayName("No debe aceptar teléfono vacío")
        void noDebeAceptarTelefonoVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("Nombre", "email@test.com", ""));
        }
        
        @Test
        @DisplayName("No debe aceptar teléfono con solo espacios")
        void noDebeAceptarTelefonoConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new DatosContacto("Nombre", "email@test.com", "   "));
        }
        
        @Test
        @DisplayName("No debe aceptar teléfono null")
        void noDebeAceptarTelefonoNull() {
            assertThrows(NullPointerException.class, 
                    () -> new DatosContacto("Nombre", "email@test.com", null));
        }
    }
    
    @Nested
    @DisplayName("Igualdad")
    class Igualdad {
        
        @Test
        @DisplayName("Datos de contacto iguales deben ser equals")
        void datosContactoIgualesDebenSerEquals() {
            var contacto1 = new DatosContacto("Juan", "juan@email.com", "123");
            var contacto2 = new DatosContacto("Juan", "juan@email.com", "123");
            
            assertEquals(contacto1, contacto2);
            assertEquals(contacto1.hashCode(), contacto2.hashCode());
        }
        
        @Test
        @DisplayName("Datos de contacto diferentes no deben ser equals")
        void datosContactoDiferentesNoDebenSerEquals() {
            var contacto1 = new DatosContacto("Juan", "juan@email.com", "123");
            var contacto2 = new DatosContacto("Pedro", "pedro@email.com", "456");
            
            assertNotEquals(contacto1, contacto2);
        }
    }
}
