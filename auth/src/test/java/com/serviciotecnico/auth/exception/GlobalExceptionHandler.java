package com.serviciotecnico.auth.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        // Simulamos el comportamiento del request
        when(webRequest.getDescription(false)).thenReturn("uri=/api/auth/login");
    }

    @Test
    void handleResponseStatusException_shouldReturnErrorDetails() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleResponseStatusException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Credenciales inválidas", response.getBody().message());
        assertEquals("uri=/api/auth/login", response.getBody().details());
    }

    @Test
    void handleValidationException_shouldReturnErrorDetails() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("loginRequest", "password", "no puede estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleValidationException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("password: no puede estar vacío", response.getBody().message());
        assertEquals("uri=/api/auth/login", response.getBody().details());
    }

    @Test
    void handleGlobalException_shouldReturnErrorDetails() {
        // Arrange
        Exception ex = new Exception("Error al generar el token JWT");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleGlobalException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        // El mensaje debe coincidir exactamente con el definido en la clase principal
        assertEquals("Error inesperado en el servicio de autenticación: Error al generar el token JWT", response.getBody().message());
        assertEquals("uri=/api/auth/login", response.getBody().details());
    }
}