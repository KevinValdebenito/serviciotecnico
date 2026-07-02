/**
 * Clase principal del microservicio de clientes.
 *
 * <p>Inicializa la aplicación Spring Boot encargada de gestionar
 * la información de los clientes del sistema.</p>
 */
package com.serviciotecnico.cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteApplication.class, args);
	}

}
