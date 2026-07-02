package com.serviciotecnico.ticket.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class TicketDtoTest {

    @Test
    void shouldCreateDtoWithAllArgsConstructorAndAccessFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();
        
        // Act: Usamos el constructor del record pasando todos los parámetros
        TicketDto dto = new TicketDto(
            id, 
            "Falla teclado", 
            "Tecla A no funciona", 
            "En progreso", 
            "Media", 
            empleadoId, 
            "prueba@correo.com"
        );

        // Assert: Validamos usando los métodos sin el prefijo "get"
        assertEquals(id, dto.id());
        assertEquals("Falla teclado", dto.title());
        assertEquals("Tecla A no funciona", dto.description());
        assertEquals("En progreso", dto.status());
        assertEquals("Media", dto.priority());
        assertEquals(empleadoId, dto.employeeId());
        assertEquals("prueba@correo.com", dto.clientEmail());
    }
}