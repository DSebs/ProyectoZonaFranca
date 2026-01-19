package com.tayronadev.infraestructura.email;

import com.tayronadev.dominio.notificacion.excepciones.EnvioCorreoException;
import com.tayronadev.dominio.notificacion.modelo.NotificacionCita;
import com.tayronadev.dominio.notificacion.puertos.NotificacionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Implementación del servicio de notificaciones usando JavaMailSender y Thymeleaf.
 * Este es el adaptador que conecta el puerto de notificaciones con la infraestructura de email.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificacionServiceImpl implements NotificacionService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${app.mail.from:noreply@zonafranca.com}")
    private String remitente;
    
    @Value("${app.mail.enabled:true}")
    private boolean emailHabilitado;
    
    @Value("${app.empresa.nombre:Zona Franca}")
    private String nombreEmpresa;
    
    @Value("${app.empresa.telefono:}")
    private String telefonoEmpresa;
    
    @Value("${app.empresa.direccion:}")
    private String direccionEmpresa;
    
    private static final DateTimeFormatter FORMATO_FECHA = 
            DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private static final DateTimeFormatter FORMATO_HORA = 
            DateTimeFormatter.ofPattern("hh:mm a", new Locale("es", "CO"));
    
    @Override
    public void enviarNotificacionCita(NotificacionCita notificacion) {
        if (!emailHabilitado) {
            log.info("Envío de emails deshabilitado. Notificación para {} no enviada.", 
                    notificacion.getDestinatarioEmail());
            return;
        }
        
        try {
            String plantilla = determinarPlantilla(notificacion);
            Context context = crearContexto(notificacion);
            String contenidoHtml = templateEngine.process(plantilla, context);
            
            enviarCorreoHtml(
                    notificacion.getDestinatarioEmail(),
                    notificacion.getAsuntoCorreo(),
                    contenidoHtml
            );
            
        } catch (Exception e) {
            log.error("Error al procesar notificación: {}", e.getMessage(), e);
            throw new EnvioCorreoException(notificacion.getDestinatarioEmail(), e.getMessage(), e);
        }
    }
    
    @Override
    public boolean estaDisponible() {
        return emailHabilitado;
    }
    
    /**
     * Determina la plantilla a usar según el tipo de notificación
     */
    private String determinarPlantilla(NotificacionCita notificacion) {
        return switch (notificacion.getTipoNotificacion()) {
            case CITA_CONFIRMADA -> "email/cita-confirmada";
            case CITA_RECHAZADA -> "email/cita-rechazada";
            case CITA_CANCELADA -> "email/cita-cancelada";
        };
    }
    
    /**
     * Crea el contexto de Thymeleaf con todas las variables necesarias
     */
    private Context crearContexto(NotificacionCita notificacion) {
        Context context = new Context(new Locale("es", "CO"));
        
        // Datos del destinatario
        context.setVariable("nombreDestinatario", notificacion.getDestinatarioNombre());
        context.setVariable("emailDestinatario", notificacion.getDestinatarioEmail());
        
        // Datos del proveedor
        context.setVariable("nombreProveedor", notificacion.getNombreProveedor());
        context.setVariable("nit", notificacion.getNit());
        
        // Datos de la cita
        context.setVariable("citaId", notificacion.getCitaId());
        context.setVariable("tipoCita", notificacion.getTipoCitaDescripcion());
        context.setVariable("fechaCita", notificacion.getFechaHoraCita().format(FORMATO_FECHA));
        context.setVariable("horaCita", notificacion.getFechaHoraCita().format(FORMATO_HORA));
        context.setVariable("fechaHoraCompleta", notificacion.getFechaHoraCita());
        
        // Observaciones/Motivo
        context.setVariable("observaciones", notificacion.getObservaciones());
        context.setVariable("tieneObservaciones", 
                notificacion.getObservaciones() != null && !notificacion.getObservaciones().isBlank());
        
        // Datos de la empresa
        context.setVariable("nombreEmpresa", nombreEmpresa);
        context.setVariable("telefonoEmpresa", telefonoEmpresa);
        context.setVariable("direccionEmpresa", direccionEmpresa);
        
        // Tipo de notificación
        context.setVariable("esConfirmacion", notificacion.esConfirmacion());
        context.setVariable("esRechazo", notificacion.esRechazo());
        context.setVariable("esCancelacion", notificacion.esCancelacion());
        
        return context;
    }
    
    /**
     * Envía un correo electrónico con contenido HTML
     */
    private void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            
            mailSender.send(mensaje);
            
            log.debug("Correo enviado exitosamente a {}", destinatario);
            
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage());
            throw new EnvioCorreoException(destinatario, "Error al construir el mensaje", e);
        }
    }
}
