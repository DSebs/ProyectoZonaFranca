package com.tayronadev.dominio.citas.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Horario - Value Object")
class HorarioTest {
    
    @Nested
    @DisplayName("Creación de Horario")
    class CreacionHorario {
        
        @Test
        @DisplayName("Debe crear horario con fecha futura")
        void debeCrearHorarioConFechaFutura() {
            var fechaFutura = LocalDateTime.now().plusDays(1).withHour(10);
            
            var horario = new Horario(fechaFutura);
            
            assertEquals(fechaFutura, horario.getFechaHora());
        }
        
        @Test
        @DisplayName("No debe crear horario con fecha pasada")
        void noDebeCrearHorarioConFechaPasada() {
            var fechaPasada = LocalDateTime.now().minusDays(1);
            
            assertThrows(IllegalArgumentException.class, () -> new Horario(fechaPasada));
        }
        
        @Test
        @DisplayName("Debe reconstruir horario con fecha pasada (desde persistencia)")
        void debeReconstruirHorarioConFechaPasada() {
            var fechaPasada = LocalDateTime.now().minusDays(1);
            
            var horario = Horario.reconstruir(fechaPasada);
            
            assertEquals(fechaPasada, horario.getFechaHora());
        }
    }
    
    @Nested
    @DisplayName("Horario Laboral")
    class HorarioLaboral {
        
        @Test
        @DisplayName("Horario a las 8 AM está dentro del horario laboral")
        void horario8AmEsDentroDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(8));
            
            assertTrue(horario.esDentroDeHorarioLaboral());
        }
        
        @Test
        @DisplayName("Horario a las 12 PM está dentro del horario laboral")
        void horario12PmEsDentroDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(12));
            
            assertTrue(horario.esDentroDeHorarioLaboral());
        }
        
        @Test
        @DisplayName("Horario a las 6 PM está dentro del horario laboral")
        void horario6PmEsDentroDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(18));
            
            assertTrue(horario.esDentroDeHorarioLaboral());
        }
        
        @Test
        @DisplayName("Horario a las 7 AM está fuera del horario laboral")
        void horario7AmEsFueraDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(7));
            
            assertFalse(horario.esDentroDeHorarioLaboral());
        }
        
        @Test
        @DisplayName("Horario a las 7 PM está fuera del horario laboral")
        void horario7PmEsFueraDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(19));
            
            assertFalse(horario.esDentroDeHorarioLaboral());
        }
        
        @Test
        @DisplayName("Horario a medianoche está fuera del horario laboral")
        void horarioMedianocheEsFueraDeLaboral() {
            var horario = Horario.reconstruir(LocalDateTime.now().withHour(0));
            
            assertFalse(horario.esDentroDeHorarioLaboral());
        }
    }
    
    @Nested
    @DisplayName("Conflictos de Horario")
    class ConflictosHorario {
        
        @Test
        @DisplayName("Horarios iguales tienen conflicto")
        void horariosIgualesTienenConflicto() {
            var fecha = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario1 = Horario.reconstruir(fecha);
            var horario2 = Horario.reconstruir(fecha);
            
            assertTrue(horario1.conflictoCon(horario2));
        }
        
        @Test
        @DisplayName("Horarios diferentes no tienen conflicto")
        void horariosDiferentesNoTienenConflicto() {
            var horario1 = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
            var horario2 = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(11));
            
            assertFalse(horario1.conflictoCon(horario2));
        }
        
        @Test
        @DisplayName("No hay conflicto con null")
        void noHayConflictoConNull() {
            var horario = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
            
            assertFalse(horario.conflictoCon(null));
        }
        
        @Test
        @DisplayName("Horarios en diferentes días no tienen conflicto")
        void horariosEnDiferentesDiasNoTienenConflicto() {
            var horario1 = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
            var horario2 = Horario.reconstruir(LocalDateTime.now().plusDays(2).withHour(10));
            
            assertFalse(horario1.conflictoCon(horario2));
        }
    }
    
    @Nested
    @DisplayName("Igualdad y HashCode")
    class IgualdadYHashCode {
        
        @Test
        @DisplayName("Horarios con misma fecha son iguales")
        void horariosConMismaFechaSonIguales() {
            var fecha = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
            var horario1 = Horario.reconstruir(fecha);
            var horario2 = Horario.reconstruir(fecha);
            
            assertEquals(horario1, horario2);
            assertEquals(horario1.hashCode(), horario2.hashCode());
        }
        
        @Test
        @DisplayName("Horarios con diferente fecha no son iguales")
        void horariosConDiferenteFechaNoSonIguales() {
            var horario1 = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(10));
            var horario2 = Horario.reconstruir(LocalDateTime.now().plusDays(1).withHour(11));
            
            assertNotEquals(horario1, horario2);
        }
    }
}
