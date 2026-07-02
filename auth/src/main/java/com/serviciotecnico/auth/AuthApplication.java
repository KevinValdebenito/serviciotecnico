package com.serviciotecnico.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {
			/**
		 * Clase principal del microservicio de autenticación.
		 *
		 * <p>Inicializa la aplicación Spring Boot encargada del registro,
		 * inicio de sesión y generación de tokens JWT.</p>
		 */

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
