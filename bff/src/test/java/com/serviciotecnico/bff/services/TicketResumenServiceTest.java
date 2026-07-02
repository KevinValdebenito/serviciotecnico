package com.serviciotecnico.bff.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.time.LocalDateTime;

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

        TicketDTO ticket = new TicketDTO(idTicket, "Pantalla rota", "Description", "Abierto", "cliente@mail.com", employeeId, LocalDateTime.now(), LocalDateTime.now());

        ClienteDTO cliente = new ClienteDTO("cliente@mail.com", "12345678-9", "Cliente Prueba", "912345678", "Direccion");

        EmpleadoDTO empleado = new EmpleadoDTO(employeeId, "tecnico1", "tecnico1@example.com", "TECNICO");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/cliente@mail.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);
        when(restClient.get().uri("http://tecnico-service/api/empleados/" + employeeId).retrieve().body(EmpleadoDTO.class)).thenReturn(empleado);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket);

        assertEquals(idTicket, result.idTicket());
        assertEquals("Pantalla rota", result.titulo());
        assertEquals("Abierto", result.estado());
        assertEquals("Cliente Prueba (Extraído de BD)", result.nombreCliente());
        assertEquals("912345678", result.telefonoCliente());
        assertEquals("tecnico1", result.nombreTecnico());
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

        TicketDTO ticket = new TicketDTO(idTicket, "Pantalla rota", "Description", "Abierto", null, null, LocalDateTime.now(), LocalDateTime.now());

        ClienteDTO cliente = new ClienteDTO("juan.perez@example.com", "12345678-9", "Cliente Default", "900000000", "Direccion");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/juan.perez@example.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket);

        assertEquals("Cliente Default (Extraído de BD)", result.nombreCliente());
        assertEquals("Técnico no asignado", result.nombreTecnico());
    }
}