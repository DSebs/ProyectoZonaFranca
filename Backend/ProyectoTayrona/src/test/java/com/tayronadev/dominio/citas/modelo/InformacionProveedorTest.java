package com.tayronadev.dominio.citas.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InformacionProveedor - Value Object")
class InformacionProveedorTest {
    
    private DatosContacto contactoValido;
    
    @BeforeEach
    void setUp() {
        contactoValido = new DatosContacto("Juan Pérez", "juan@email.com", "3001234567");
    }
    
    @Nested
    @DisplayName("Creación válida")
    class CreacionValida {
        
        @Test
        @DisplayName("Debe crear información de proveedor válida")
        void debeCrearInformacionProveedorValida() {
            var proveedor = new InformacionProveedor(
                    "Proveedor ABC", 
                    "900123456-1", 
                    "OC-001", 
                    contactoValido
            );
            
            assertEquals("Proveedor ABC", proveedor.getNombreProveedor());
            assertEquals("900123456-1", proveedor.getNit());
            assertEquals("OC-001", proveedor.getNumeroOrdenCompra());
            assertEquals(contactoValido, proveedor.getResponsable());
        }
    }
    
    @Nested
    @DisplayName("Validaciones de nombre de proveedor")
    class ValidacionesNombreProveedor {
        
        @Test
        @DisplayName("No debe aceptar nombre vacío")
        void noDebeAceptarNombreVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("", "900123456", "OC-001", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre con solo espacios")
        void noDebeAceptarNombreConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("   ", "900123456", "OC-001", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar nombre null")
        void noDebeAceptarNombreNull() {
            assertThrows(NullPointerException.class, 
                    () -> new InformacionProveedor(null, "900123456", "OC-001", contactoValido));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de NIT")
    class ValidacionesNit {
        
        @Test
        @DisplayName("No debe aceptar NIT vacío")
        void noDebeAceptarNitVacio() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("Proveedor", "", "OC-001", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar NIT con solo espacios")
        void noDebeAceptarNitConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("Proveedor", "   ", "OC-001", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar NIT null")
        void noDebeAceptarNitNull() {
            assertThrows(NullPointerException.class, 
                    () -> new InformacionProveedor("Proveedor", null, "OC-001", contactoValido));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de número de orden de compra")
    class ValidacionesOrdenCompra {
        
        @Test
        @DisplayName("No debe aceptar orden de compra vacía")
        void noDebeAceptarOrdenCompraVacia() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("Proveedor", "900123456", "", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar orden de compra con solo espacios")
        void noDebeAceptarOrdenCompraConSoloEspacios() {
            assertThrows(IllegalArgumentException.class, 
                    () -> new InformacionProveedor("Proveedor", "900123456", "   ", contactoValido));
        }
        
        @Test
        @DisplayName("No debe aceptar orden de compra null")
        void noDebeAceptarOrdenCompraNull() {
            assertThrows(NullPointerException.class, 
                    () -> new InformacionProveedor("Proveedor", "900123456", null, contactoValido));
        }
    }
    
    @Nested
    @DisplayName("Validaciones de responsable")
    class ValidacionesResponsable {
        
        @Test
        @DisplayName("No debe aceptar responsable null")
        void noDebeAceptarResponsableNull() {
            assertThrows(NullPointerException.class, 
                    () -> new InformacionProveedor("Proveedor", "900123456", "OC-001", null));
        }
    }
    
    @Nested
    @DisplayName("Igualdad")
    class Igualdad {
        
        @Test
        @DisplayName("Proveedores con mismos datos deben ser iguales")
        void proveedoresConMismosDatosDebenSerIguales() {
            var proveedor1 = new InformacionProveedor("ABC", "123", "OC-1", contactoValido);
            var proveedor2 = new InformacionProveedor("ABC", "123", "OC-1", contactoValido);
            
            assertEquals(proveedor1, proveedor2);
            assertEquals(proveedor1.hashCode(), proveedor2.hashCode());
        }
        
        @Test
        @DisplayName("Proveedores con diferentes datos no deben ser iguales")
        void proveedoresConDiferentesDatosNoDebenSerIguales() {
            var proveedor1 = new InformacionProveedor("ABC", "123", "OC-1", contactoValido);
            var proveedor2 = new InformacionProveedor("XYZ", "456", "OC-2", contactoValido);
            
            assertNotEquals(proveedor1, proveedor2);
        }
    }
}
