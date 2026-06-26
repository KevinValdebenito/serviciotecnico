package com.serviciotecnico.empleado.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CustomExceptionsTest {

    @Test
    void testApiException() {
        // Al ser abstracta, añadimos "{}" al final para instanciarla como clase anónima
        ApiException exception = new ApiException("ERR_500", "Error en la API", HttpStatus.INTERNAL_SERVER_ERROR) {};
        
        // Comprobamos que guarde los tres datos correctamente
        assertEquals("ERR_500", exception.getCode());
        assertEquals("Error en la API", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertNull(exception.getCause());
    }

    @Test
    void testAuthenticationException() {
        AuthenticationException exception = new AuthenticationException("Fallo de autenticación");
        assertEquals("Fallo de autenticación", exception.getMessage());
    }

    @Test
    void testAuthorizationException() {
        AuthorizationException exception = new AuthorizationException("No autorizado");
        assertEquals("No autorizado", exception.getMessage());
    }

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Recurso no encontrado");
        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    @Test
    void testValidationException() {
        ValidationException exception = new ValidationException("Datos inválidos");
        assertEquals("Datos inválidos", exception.getMessage());
    }
}