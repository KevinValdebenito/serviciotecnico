package com.serviciotecnico.empleado.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.serviciotecnico.empleado.dto.ApiResponse;
import com.serviciotecnico.empleado.dto.AuthResponse;
import com.serviciotecnico.empleado.dto.LoginRequest;
import com.serviciotecnico.empleado.dto.RegisterRequest;
import com.serviciotecnico.empleado.service.AuthService;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerShouldReturnCreatedResponse() {
        RegisterRequest request = new RegisterRequest("kevinval", "password123", "kevin@correo.com", "ADMIN");
        AuthResponse authResponse = new AuthResponse("token-generado", 3600L, "kevin@correo.com");

        when(authService.register(request)).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.register(request);

        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getBody().success());
        assertEquals("token-generado", response.getBody().data().token());
        verify(authService).register(request);
    }

    @Test
    void loginShouldReturnOkResponse() {
        LoginRequest request = new LoginRequest("kevin@correo.com", "password123");
        AuthResponse authResponse = new AuthResponse("token-generado", 3600L, "kevin@correo.com");

        when(authService.login(request)).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().success());
        assertEquals("token-generado", response.getBody().data().token());
        verify(authService).login(request);
    }

    @Test
    void validateShouldReturnTrueWhenTokenIsValid() {
        when(authService.validateToken("token-valido")).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response = authController.validate("Bearer token-valido");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().success());
        assertEquals(true, response.getBody().data());
        verify(authService).validateToken("token-valido");
    }

    @Test
    void validateShouldThrowWhenHeaderIsMissing() {
        ValidationException exception = assertThrows(ValidationException.class, () -> authController.validate(null));

        assertEquals("Missing or invalid Authorizarion header", exception.getMessage());
    }

    @Test
    void validateShouldThrowWhenHeaderDoesNotStartWithBearer() {
        ValidationException exception = assertThrows(ValidationException.class, () -> authController.validate("Basic abc123"));

        assertEquals("Missing or invalid Authorizarion header", exception.getMessage());
    }
}