package com.serviciotecnico.bff.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class BffDtoTest {

    @Test
    void shouldExposeAuthResponseToken() {
        AuthResponse response = new AuthResponse("token-123");

        assertEquals("token-123", response.token());
    }

    @Test
    void shouldExposeLoginRequestValues() {
        LoginRequest request = new LoginRequest("user@mail.com", "secret");

        assertEquals("user@mail.com", request.email());
        assertEquals("secret", request.password());
    }

    @Test
    void shouldExposeRegisterRequestValues() {
        RegisterRequest request = new RegisterRequest("user@mail.com", "secret");

        assertEquals("user@mail.com", request.email());
        assertEquals("secret", request.password());
    }

    @Test
    void shouldExposeTicketDtoValues() {
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        
        TicketDTO ticket = new TicketDTO(id, "Pantalla rota", null, "Abierto", "cliente@mail.com", employeeId, null, null);

        assertEquals(id, ticket.id());
        assertEquals("Pantalla rota", ticket.title());
        assertEquals("Abierto", ticket.status());
        assertEquals("cliente@mail.com", ticket.clientEmail());
        assertEquals(employeeId, ticket.employeeId());
    }

    @Test
    void shouldExposeEmpleadoDtoValues() {
        UUID id = UUID.randomUUID();
        EmpleadoDTO empleado = new EmpleadoDTO(id, "tecnico1", null, null);

        assertEquals(id, empleado.id());
        assertEquals("tecnico1", empleado.username());
    }

    @Test
    void shouldExposeClienteDtoValues() {
        ClienteDTO cliente = new ClienteDTO("cliente@mail.com", "11.222.333-4", "Cliente Prueba", "912345678", "Santiago");

        assertEquals("cliente@mail.com", cliente.email());
        assertEquals("11.222.333-4", cliente.rut());
        assertEquals("Cliente Prueba", cliente.nombreCompleto());
        assertEquals("912345678", cliente.telefono());
        assertEquals("Santiago", cliente.direccion());
    }

    @Test
    void shouldExposeTicketResumenDtoValues() {
        UUID idTicket = UUID.randomUUID();
        TicketResumenDTO resumen = new TicketResumenDTO(idTicket, "Pantalla rota", "Abierto", "Cliente Prueba", "912345678", "tecnico1");

        assertEquals(idTicket, resumen.idTicket());
        assertEquals("Pantalla rota", resumen.titulo());
        assertEquals("Abierto", resumen.estado());
        assertEquals("Cliente Prueba", resumen.nombreCliente());
        assertEquals("912345678", resumen.telefonoCliente());
        assertEquals("tecnico1", resumen.nombreTecnico());
    }
}