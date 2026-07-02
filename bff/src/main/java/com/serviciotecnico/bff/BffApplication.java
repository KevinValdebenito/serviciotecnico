/**
 * Clase principal del BFF, Backend For Frontend.
 *
 * <p>Inicializa el servicio que actúa como punto de entrada
 * para el frontend y centraliza la comunicación con otros microservicios.</p>
 */
package com.serviciotecnico.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BffApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffApplication.class, args);
	}

}
