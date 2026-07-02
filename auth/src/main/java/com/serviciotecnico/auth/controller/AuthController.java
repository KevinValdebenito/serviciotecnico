/**
 * Controlador REST encargado de exponer los endpoints de autenticación.
 *
 * <p>Permite registrar usuarios e iniciar sesión mediante solicitudes HTTP.
 * La lógica de negocio es delegada al servicio de autenticación.</p>
 */
package com.serviciotecnico.auth.controller;

import com.serviciotecnico.auth.dto.AuthResponse;
import com.serviciotecnico.auth.dto.LoginRequest;
import com.serviciotecnico.auth.dto.RegisterRequest;
import com.serviciotecnico.auth.service.AuthService;
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