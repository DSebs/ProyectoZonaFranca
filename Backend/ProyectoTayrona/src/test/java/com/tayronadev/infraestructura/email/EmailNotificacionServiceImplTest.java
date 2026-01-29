package com.tayronadev.infraestructura.email;

import com.tayronadev.dominio.notificacion.excepciones.EnvioCorreoException;
import com.tayronadev.dominio.notificacion.modelo.NotificacionCita;
import com.tayronadev.dominio.notificacion.modelo.TipoNotificacion;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailNotificacionServiceImpl - Adaptador de Infraestructura")
class EmailNotificacionServiceImplTest {
    
    @Mock
    private JavaMailSender mailSender;
    
    @Mock
    private TemplateEngine templateEngine;
    
    @Mock
    private MimeMessage mimeMessage;
    
    private EmailNotificacionServiceImpl emailService;
    
    @BeforeEach
    void setUp() {
        emailService = new EmailNotificacionServiceImpl(mailSender, templateEngine);
        
        // Configurar valores por defecto usando reflection
        ReflectionTestUtils.setField(emailService, "remitente", "noreply@test.com");
        ReflectionTestUtils.setField(emailService, "emailHabilitado", true);
        ReflectionTestUtils.setField(emailService, "nombreEmpresa", "Test Empresa");
        ReflectionTestUtils.setField(emailService, "telefonoEmpresa", "123456");
        ReflectionTestUtils.setField(emailService, "direccionEmpresa", "Dirección Test");
    }
    
    @Nested
    @DisplayName("Disponibilidad del servicio")
    class DisponibilidadServicio {
        
        @Test
        @DisplayName("Debe reportar disponible cuando está habilitado")
        void debeReportarDisponibleCuandoHabilitado() {
            ReflectionTestUtils.setField(emailService, "emailHabilitado", true);
            
            assertTrue(emailService.estaDisponible());
        }
        
        @Test
        @DisplayName("Debe reportar no disponible cuando está deshabilitado")
        void debeReportarNoDisponibleCuandoDeshabilitado() {
            ReflectionTestUtils.setField(emailService, "emailHabilitado", false);
            
            assertFalse(emailService.estaDisponible());
        }
    }
    
    @Nested
    @DisplayName("Envío de notificaciones")
    class EnvioNotificaciones {
        
        @Test
        @DisplayName("No debe enviar correo cuando está deshabilitado")
        void noDebeEnviarCorreoCuandoDeshabilitado() {
            // Given
            ReflectionTestUtils.setField(emailService, "emailHabilitado", false);
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA);
            
            // When
            emailService.enviarNotificacionCita(notificacion);
            
            // Then
            verify(mailSender, never()).send(any(MimeMessage.class));
            verify(templateEngine, never()).process(anyString(), any(Context.class));
        }
        
        @Test
        @DisplayName("Debe procesar plantilla correcta para confirmación")
        void debeProcesarPlantillaCorrectaParaConfirmacion() {
            // Given
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Test</html>");
            
            // When
            emailService.enviarNotificacionCita(notificacion);
            
            // Then
            ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
            verify(templateEngine).process(templateCaptor.capture(), any(Context.class));
            assertEquals("email/cita-confirmada", templateCaptor.getValue());
        }
        
        @Test
        @DisplayName("Debe procesar plantilla correcta para rechazo")
        void debeProcesarPlantillaCorrectaParaRechazo() {
            // Given
            var notificacion = crearNotificacion(TipoNotificacion.CITA_RECHAZADA);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Test</html>");
            
            // When
            emailService.enviarNotificacionCita(notificacion);
            
            // Then
            ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
            verify(templateEngine).process(templateCaptor.capture(), any(Context.class));
            assertEquals("email/cita-rechazada", templateCaptor.getValue());
        }
        
        @Test
        @DisplayName("Debe procesar plantilla correcta para cancelación")
        void debeProcesarPlantillaCorrectaParaCancelacion() {
            // Given
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CANCELADA);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Test</html>");
            
            // When
            emailService.enviarNotificacionCita(notificacion);
            
            // Then
            ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
            verify(templateEngine).process(templateCaptor.capture(), any(Context.class));
            assertEquals("email/cita-cancelada", templateCaptor.getValue());
        }
        
        @Test
        @DisplayName("Debe enviar correo a través de JavaMailSender")
        void debeEnviarCorreoATravesDeMailSender() {
            // Given
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Test</html>");
            
            // When
            emailService.enviarNotificacionCita(notificacion);
            
            // Then
            verify(mailSender).send(mimeMessage);
        }
    }
    
    @Nested
    @DisplayName("Manejo de errores")
    class ManejoErrores {
        
        @Test
        @DisplayName("Debe lanzar EnvioCorreoException cuando falla el procesamiento de plantilla")
        void debeLanzarExcepcionCuandoFallaProcesamiento() {
            // Given
            var notificacion = crearNotificacion(TipoNotificacion.CITA_CONFIRMADA);
            when(templateEngine.process(anyString(), any(Context.class)))
                    .thenThrow(new RuntimeException("Error de plantilla"));
            
            // When & Then
            assertThrows(EnvioCorreoException.class, 
                    () -> emailService.enviarNotificacionCita(notificacion));
        }
    }
    
    // Método auxiliar
    private NotificacionCita crearNotificacion(TipoNotificacion tipo) {
        return NotificacionCita.builder()
                .destinatarioEmail("destinatario@test.com")
                .destinatarioNombre("Test User")
                .nombreProveedor("Proveedor Test")
                .nit("123456789")
                .citaId("cita-test-123")
                .tipoCita("ENTREGA")
                .tipoCitaDescripcion("Entrega de mercancía")
                .fechaHoraCita(LocalDateTime.of(2025, 1, 20, 10, 0))
                .tipoNotificacion(tipo)
                .observaciones("Observaciones de prueba")
                .build();
    }
}
