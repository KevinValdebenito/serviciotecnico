package com.serviciotecnico.bff.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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

@SuppressWarnings({"unchecked", "rawtypes"})
class TicketResumenServiceTest {

    private static final String AUTH_HEADER = "Bearer test-token";

    private RestClient restClient;
    private RestClient.RequestHeadersUriSpec uriSpec;
    private TicketResumenService service;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class);
        uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        service = new TicketResumenService(restClient, "http://ticket-service", "http://cliente-service", "http://tecnico-service");

        when(restClient.get()).thenReturn(uriSpec);
    }

    /**
     * Arma un mock de la cadena .uri(url).header(...).retrieve().body(tipo)
     * para que devuelva el objeto indicado.
     */
    private <T> void stubUri(String url, Class<T> bodyType, T response) {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(uriSpec.uri(url)).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(bodyType)).thenReturn(response);
    }

    private void stubUriThrows(String url) {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);

        when(uriSpec.uri(url)).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenThrow(new RuntimeException("not found"));
    }

    @Test
    void shouldBuildResumenWhenAllServicesRespond() {
        UUID idTicket = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        TicketDTO ticket = new TicketDTO(idTicket, "Pantalla rota", "Description", "Abierto", "cliente@mail.com", employeeId, LocalDateTime.now(), LocalDateTime.now());
        ClienteDTO cliente = new ClienteDTO("cliente@mail.com", "12345678-9", "Cliente Prueba", "912345678", "Direccion");
        EmpleadoDTO empleado = new EmpleadoDTO(employeeId, "tecnico1", "tecnico1@example.com", "TECNICO");

        stubUri("http://ticket-service/api/tickets/" + idTicket, TicketDTO.class, ticket);
        stubUri("http://cliente-service/api/clientes/cliente@mail.com", ClienteDTO.class, cliente);
        stubUri("http://tecnico-service/api/empleados/" + employeeId, EmpleadoDTO.class, empleado);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket, AUTH_HEADER);

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

        stubUriThrows("http://ticket-service/api/tickets/" + idTicket);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.obtenerResumenCompleto(idTicket, AUTH_HEADER));

        assertEquals("Ticket no encontrado con id: " + idTicket, exception.getReason());
    }

    @Test
    void shouldUseDefaultClientEmailWhenTicketHasNoClientEmail() {
        UUID idTicket = UUID.randomUUID();

        TicketDTO ticket = new TicketDTO(idTicket, "Pantalla rota", "Description", "Abierto", null, null, LocalDateTime.now(), LocalDateTime.now());
        ClienteDTO cliente = new ClienteDTO("juan.perez@example.com", "12345678-9", "Cliente Default", "900000000", "Direccion");

        stubUri("http://ticket-service/api/tickets/" + idTicket, TicketDTO.class, ticket);
        stubUri("http://cliente-service/api/clientes/juan.perez@example.com", ClienteDTO.class, cliente);

        TicketResumenDTO result = service.obtenerResumenCompleto(idTicket, AUTH_HEADER);

        assertEquals("Cliente Default (Extraído de BD)", result.nombreCliente());
        assertEquals("Técnico no asignado", result.nombreTecnico());
    }
}