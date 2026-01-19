package com.tayronadev.dominio.notificacion.excepciones;

/**
 * Excepción lanzada cuando falla el envío de un correo electrónico
 */
public class EnvioCorreoException extends NotificacionException {
    
    private final String destinatario;
    
    public EnvioCorreoException(String destinatario, String mensaje) {
        super(String.format("Error al enviar correo a %s: %s", destinatario, mensaje));
        this.destinatario = destinatario;
    }
    
    public EnvioCorreoException(String destinatario, String mensaje, Throwable causa) {
        super(String.format("Error al enviar correo a %s: %s", destinatario, mensaje), causa);
        this.destinatario = destinatario;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
}
