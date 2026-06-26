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
        TicketDTO ticket = new TicketDTO();
        UUID id = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        ticket.setId(id);
        ticket.setTitle("Pantalla rota");
        ticket.setStatus("Abierto");
        ticket.setPriority("Alta");
        ticket.setClientEmail("cliente@mail.com");
        ticket.setEmployeeId(employeeId);

        assertEquals(id, ticket.getId());
        assertEquals("Pantalla rota", ticket.getTitle());
        assertEquals("Abierto", ticket.getStatus());
        assertEquals("Alta", ticket.getPriority());
        assertEquals("cliente@mail.com", ticket.getClientEmail());
        assertEquals(employeeId, ticket.getEmployeeId());
    }

    @Test
    void shouldExposeEmpleadoDtoValues() {
        EmpleadoDTO empleado = new EmpleadoDTO();
        UUID id = UUID.randomUUID();

        empleado.setId(id);
        empleado.setUsername("tecnico1");

        assertEquals(id, empleado.getId());
        assertEquals("tecnico1", empleado.getUsername());
    }

    @Test
    void shouldExposeClienteDtoValues() {
        ClienteDTO cliente = new ClienteDTO();

        cliente.setEmail("cliente@mail.com");
        cliente.setRut("11.222.333-4");
        cliente.setNombreCompleto("Cliente Prueba");
        cliente.setTelefono("912345678");
        cliente.setDireccion("Santiago");

        assertEquals("cliente@mail.com", cliente.getEmail());
        assertEquals("11.222.333-4", cliente.getRut());
        assertEquals("Cliente Prueba", cliente.getNombreCompleto());
        assertEquals("912345678", cliente.getTelefono());
        assertEquals("Santiago", cliente.getDireccion());
    }

    @Test
    void shouldExposeTicketResumenDtoValues() {
        TicketResumenDTO resumen = new TicketResumenDTO();
        UUID idTicket = UUID.randomUUID();

        resumen.setIdTicket(idTicket);
        resumen.setTitulo("Pantalla rota");
        resumen.setEstado("Abierto");
        resumen.setNombreCliente("Cliente Prueba");
        resumen.setTelefonoCliente("912345678");
        resumen.setNombreTecnico("tecnico1");

        assertEquals(idTicket, resumen.getIdTicket());
        assertEquals("Pantalla rota", resumen.getTitulo());
        assertEquals("Abierto", resumen.getEstado());
        assertEquals("Cliente Prueba", resumen.getNombreCliente());
        assertEquals("912345678", resumen.getTelefonoCliente());
        assertEquals("tecnico1", resumen.getNombreTecnico());
    }
}