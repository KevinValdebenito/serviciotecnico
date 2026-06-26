package com.serviciotecnico.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import com.serviciotecnico.bff.dto.ClienteDTO;
import com.serviciotecnico.bff.dto.EmpleadoDTO;
import com.serviciotecnico.bff.dto.TicketDTO;
import com.serviciotecnico.bff.dto.TicketResumenDTO;

class TicketBffControllerTest {

    private RestClient restClient;
    private TicketBffController controller;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        controller = new TicketBffController();
        ReflectionTestUtils.setField(controller, "restClient", restClient);
        ReflectionTestUtils.setField(controller, "ticketServiceUrl", "http://ticket-service");
        ReflectionTestUtils.setField(controller, "clienteServiceUrl", "http://cliente-service");
        ReflectionTestUtils.setField(controller, "tecnicoServiceUrl", "http://tecnico-service");
    }

    @Test
    void shouldBuildResumenWhenAllServicesRespond() {
        UUID idTicket = UUID.randomUUID();
        TicketDTO ticket = new TicketDTO();
        ticket.setTitle("Pantalla rota");
        ticket.setStatus("Abierto");
        ticket.setClientEmail("cliente@mail.com");
        ticket.setEmployeeId(UUID.randomUUID());

        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombreCompleto("Cliente Prueba");
        cliente.setTelefono("912345678");

        EmpleadoDTO empleado = new EmpleadoDTO();
        empleado.setUsername("tecnico1");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/cliente@mail.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);
        when(restClient.get().uri("http://tecnico-service/api/empleados/" + ticket.getEmployeeId()).retrieve().body(EmpleadoDTO.class)).thenReturn(empleado);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket).getBody();

        assertNotNull(result);
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

        assertEquals(404, controller.obtenerResumenCompleto(idTicket).getStatusCode().value());
    }

    @Test
    void shouldUseDefaultClientEmailWhenTicketHasNoClientEmail() {
        UUID idTicket = UUID.randomUUID();
        TicketDTO ticket = new TicketDTO();
        ticket.setTitle("Pantalla rota");
        ticket.setStatus("Abierto");
        ticket.setEmployeeId(null);

        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombreCompleto("Cliente Default");
        cliente.setTelefono("900000000");

        when(restClient.get().uri("http://ticket-service/api/tickets/" + idTicket).retrieve().body(TicketDTO.class)).thenReturn(ticket);
        when(restClient.get().uri("http://cliente-service/api/clientes/juan.perez@example.com").retrieve().body(ClienteDTO.class)).thenReturn(cliente);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket).getBody();

        assertNotNull(result);
        assertEquals("Cliente Default (Extraído de BD)", result.getNombreCliente());
        assertEquals("Técnico no asignado", result.getNombreTecnico());
    }
}