/**
 * Cliente HTTP encargado de comunicarse con el microservicio de autenticación.
 *
 * <p>Permite enviar solicitudes de login y registro desde el BFF
 * hacia el servicio de autenticación.</p>
 */
package com.serviciotecnico.bff.client;

import com.serviciotecnico.bff.dto.AuthResponse;
import com.serviciotecnico.bff.dto.LoginRequest;
import com.serviciotecnico.bff.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

	private final RestClient restClient;
	private final String authBaseUrl;

	public AuthClient(RestClient restClient, @Value("${auth.service.base-url}") String authBaseUrl) {
		this.restClient = restClient;
		this.authBaseUrl = authBaseUrl;
	}

	public AuthResponse login(LoginRequest request) {
		return restClient.post()
			.uri(authBaseUrl + "/login")
			.body(request)
			.retrieve()
			.body(AuthResponse.class);
	}

	public AuthResponse register(RegisterRequest request) {
		return restClient.post()
			.uri(authBaseUrl + "/register")
			.body(request)
			.retrieve()
			.body(AuthResponse.class);
	}
}