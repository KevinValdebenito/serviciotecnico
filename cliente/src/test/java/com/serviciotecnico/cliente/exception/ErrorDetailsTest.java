package com.serviciotecnico.cliente.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorDetailsTest {

    @Test
    void shouldCreateErrorDetailsAndAccessFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String message = "Error de prueba cliente";
        String details = "Detalles del error cliente";

        // Act
        ErrorDetails errorDetails = new ErrorDetails(now, message, details);

        // Assert
        assertNotNull(errorDetails);
        assertEquals(now, errorDetails.timestamp());
        assertEquals(message, errorDetails.message());
        assertEquals(details, errorDetails.details());
    }
}