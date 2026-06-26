package com.serviciotecnico.empleado.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.serviciotecnico.empleado.dto.ApiResponse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleApiException() {
        // Arrange
        WebRequest request = mock(WebRequest.class);
        ApiException ex = mock(ApiException.class);
        
        // Simulamos lo que devuelve la excepción
        when(ex.getCode()).thenReturn("ERR_001");
        when(ex.getMessage()).thenReturn("Error de prueba");
        when(ex.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<ApiResponse<?>> response = handler.handleApiException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("ERR_001", response.getBody().error().code());
        assertEquals("Error de prueba", response.getBody().error().message());
    }

    @Test
    void testHandleGlobalException() {
        // Arrange
        WebRequest request = mock(WebRequest.class);
        Exception ex = new Exception("Fallo en la base de datos");

        // Act
        ResponseEntity<ApiResponse<?>> response = handler.handleGlobalException(ex, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().error().code());
        assertEquals("An unexpected error occurred: Fallo en la base de datos", response.getBody().error().message());
    }
}