/**
 * Clase principal del microservicio de tickets.
 *
 * <p>Inicializa la aplicación encargada de gestionar las solicitudes
 * de atención del sistema de servicio técnico.</p>
 */
package com.serviciotecnico.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketApplication.class, args);
	}

}
