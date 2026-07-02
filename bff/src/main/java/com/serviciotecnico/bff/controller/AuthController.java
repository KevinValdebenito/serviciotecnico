/**
 * Controlador REST del BFF encargado de exponer endpoints de autenticación.
 *
 * <p>Recibe las solicitudes del cliente externo y las delega al servicio
 * del BFF, el cual se comunica con el microservicio de autenticación.</p>
 */
package com.serviciotecnico.bff.controller;

import com.serviciotecnico.bff.dto.AuthResponse;
import com.serviciotecnico.bff.dto.LoginRequest;
import com.serviciotecnico.bff.dto.RegisterRequest;
import com.serviciotecnico.bff.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return authService.register(request);
	}
}