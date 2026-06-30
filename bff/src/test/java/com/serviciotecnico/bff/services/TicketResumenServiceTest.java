package com.serviciotecnico.bff.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.bff.dto.ClienteDTO;
import com.serviciotecnico.bff.dto.EmpleadoDTO;
import com.serviciotecnico.bff.dto.TicketDTO;
import com.serviciotecnico.bff.dto.TicketResumenDTO;

class TicketResumenServiceTest {

    private RestClient restClient;
    private TicketResumenService service;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        service = new TicketResumenService(restClient, "http://ticket-service", "http://cliente-service", "http://tecnico-service");
    }

    @Test
    void shouldBuildResumenWhenAllServicesRespond() {
        UUID idTicket = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        TicketDTO ticket = new TicketDTO();
        ticket.setTitle("Pantalla rota");
        ticket.setStatus("Abierto");
        ticket.setClientEmail("cliente@mail.com");
        ticket.setEmployeeId(employeeId);

        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombreCompleto("Cliente Prueba");
        cliente.setTelefono("912345678");

        EmpleadoDTO empleado = new EmpleadoDTO();
        empleado.setUsername("tecnico1");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/cliente@mail.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);
        when(restClient.get().uri("http://tecnico-service/api/empleados/" + employeeId).retrieve().body(EmpleadoDTO.class)).thenReturn(empleado);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket);

        assertEquals(idTicket, result.getIdTicket());
        assertEquals("Pantalla rota", result.getTitulo());
        assertEquals("Abierto", result.getEstado());
        assertEquals("Cliente Prueba (Extraído de BD)", result.getNombreCliente());
        assertEquals("912345678", result.getTelefonoCliente());
        assertEquals("tecnico1", result.getNombreTecnico());
    }

    @Test
    void shouldReturnNotFoundWhenTicketDoesNotExist() {
        UUID idTicket = UUID.randomUUID();

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class))
            .thenThrow(new RuntimeException("not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.obtenerResumenCompleto(idTicket));

        assertEquals("Ticket no encontrado con id: " + idTicket, exception.getReason());
    }

    @Test
    void shouldUseDefaultClientEmailWhenTicketHasNoClientEmail() {
        UUID idTicket = UUID.randomUUID();

        TicketDTO ticket = new TicketDTO();
        ticket.setTitle("Pantalla rota");
        ticket.setStatus("Abierto");

        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombreCompleto("Cliente Default");
        cliente.setTelefono("900000000");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/juan.perez@example.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket);

        assertEquals("Cliente Default (Extraído de BD)", result.getNombreCliente());
        assertEquals("Técnico no asignado", result.getNombreTecnico());
    }
}