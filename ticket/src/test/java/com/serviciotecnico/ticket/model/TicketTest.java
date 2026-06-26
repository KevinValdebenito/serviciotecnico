package com.serviciotecnico.ticket.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class TicketTest {

    @Test
    void shouldCreateTicketWithNoArgsConstructorAndSetters() {
        // Arrange
        Ticket ticket = new Ticket();
        UUID id = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();


        ticket.setId(id);
        ticket.setTitle("Problema de red");
        ticket.setDescription("No hay internet");
        ticket.setStatus("Abierto");
        ticket.setPriority("Alta");
        ticket.setEmployeeId(empleadoId);
        ticket.setClientEmail("test@correo.com");

        assertEquals(id, ticket.getId());
        assertEquals("Problema de red", ticket.getTitle());
        assertEquals("No hay internet", ticket.getDescription());
        assertEquals("Abierto", ticket.getStatus());
        assertEquals("Alta", ticket.getPriority());
        assertEquals(empleadoId, ticket.getEmployeeId());
        assertEquals("test@correo.com", ticket.getClientEmail());
    }

    @Test
    void shouldCreateTicketWithAllArgsConstructor() {

        UUID empleadoId = UUID.randomUUID();
        Ticket ticket = new Ticket("Falla de hardware", "Pantalla azul", "En progreso", "Media", empleadoId, "cliente@correo.com");

        assertEquals("Falla de hardware", ticket.getTitle());
        assertEquals("Pantalla azul", ticket.getDescription());
        assertEquals("En progreso", ticket.getStatus());
        assertEquals("Media", ticket.getPriority());
        assertEquals(empleadoId, ticket.getEmployeeId());
        assertEquals("cliente@correo.com", ticket.getClientEmail());
        assertNull(ticket.getId()); 
    }
}