package com.serviciotecnico.cliente.exception;

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
        when(webRequest.getDescription(false)).thenReturn("uri=/api/clientes");
    }

    @Test
    void handleResponseStatusException_shouldReturnErrorDetails() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleResponseStatusException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cliente no encontrado", response.getBody().message());
        assertEquals("uri=/api/clientes", response.getBody().details());
    }

    @Test
    void handleValidationException_shouldReturnErrorDetails() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("clienteDto", "email", "formato inválido");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleValidationException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("email: formato inválido", response.getBody().message());
        assertEquals("uri=/api/clientes", response.getBody().details());
    }

    @Test
    void handleGlobalException_shouldReturnErrorDetails() {
        // Arrange
        Exception ex = new Exception("Fallo de base de datos");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleGlobalException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        // Aquí debe coincidir exactamente con el mensaje que tienes en tu clase GlobalExceptionHandler
        assertEquals("Error inesperado en el servicio de clientes: Fallo de base de datos", response.getBody().message());
        assertEquals("uri=/api/clientes", response.getBody().details());
    }
}