package com.serviciotecnico.ticket.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class TicketDtoTest {

    @Test
    void shouldCreateDtoWithNoArgsConstructorAndSetters() {
        // Arrange
        TicketDto dto = new TicketDto();
        UUID id = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();

        // Act
        dto.setId(id);
        dto.setTitle("Pantalla rota");
        dto.setDescription("Se cayó la laptop al piso");
        dto.setStatus("Abierto");
        dto.setPriority("Alta");
        dto.setEmployeeId(empleadoId);
        dto.setClientEmail("cliente@correo.com");

        // Assert
        assertEquals(id, dto.getId());
        assertEquals("Pantalla rota", dto.getTitle());
        assertEquals("Se cayó la laptop al piso", dto.getDescription());
        assertEquals("Abierto", dto.getStatus());
        assertEquals("Alta", dto.getPriority());
        assertEquals(empleadoId, dto.getEmployeeId());
        assertEquals("cliente@correo.com", dto.getClientEmail());
    }

    @Test
    void shouldCreateDtoWithAllArgsConstructor() {
        // Arrange & Act
        UUID id = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();
        TicketDto dto = new TicketDto(id, "Falla teclado", "Tecla A no funciona", "En progreso", "Media", empleadoId, "prueba@correo.com");

        // Assert
        assertEquals(id, dto.getId());
        assertEquals("Falla teclado", dto.getTitle());
        assertEquals("Tecla A no funciona", dto.getDescription());
        assertEquals("En progreso", dto.getStatus());
        assertEquals("Media", dto.getPriority());
        assertEquals(empleadoId, dto.getEmployeeId());
        assertEquals("prueba@correo.com", dto.getClientEmail());
    }
}