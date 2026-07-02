package com.serviciotecnico.bff.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorDetailsTest {

    @Test
    void shouldCreateErrorDetailsAndAccessFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String message = "Error de prueba BFF";
        String details = "Detalles del error BFF";

        // Act
        ErrorDetails errorDetails = new ErrorDetails(now, message, details);

        // Assert
        assertNotNull(errorDetails);
        assertEquals(now, errorDetails.timestamp());
        assertEquals(message, errorDetails.message());
        assertEquals(details, errorDetails.details());
    }
}