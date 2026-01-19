package com.tayronadev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Clase principal de la aplicación de agendamiento de citas.
 * Habilita el procesamiento asíncrono para el envío de notificaciones.
 */
@SpringBootApplication
@EnableAsync
public class CitasApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitasApplication.class, args);
    }
}
