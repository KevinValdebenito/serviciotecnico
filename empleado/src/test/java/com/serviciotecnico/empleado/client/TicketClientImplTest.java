package com.serviciotecnico.empleado.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.serviciotecnico.empleado.dto.TicketDto;

@SuppressWarnings({"unchecked", "rawtypes"})
class TicketClientImplTest {

    private RestClient restClient;
    private RestClient.RequestHeadersUriSpec getSpec;
    private RestClient.RequestBodyUriSpec postSpec;
    private RestClient.RequestBodyUriSpec putSpec;
    private RestClient.RequestHeadersUriSpec deleteSpec;

    private TicketClientImpl ticketClient;

    @BeforeEach
    void setUp() throws Exception {
        ticketClient = new TicketClientImpl("http://ticket-service/api");

        restClient = mock(RestClient.class);
        getSpec = mock(RestClient.RequestHeadersUriSpec.class);
        postSpec = mock(RestClient.RequestBodyUriSpec.class);
        putSpec = mock(RestClient.RequestBodyUriSpec.class);
        deleteSpec = mock(RestClient.RequestHeadersUriSpec.class);

        when(restClient.get()).thenReturn(getSpec);
        when(restClient.post()).thenReturn(postSpec);
        when(restClient.put()).thenReturn(putSpec);
        when(restClient.delete()).thenReturn(deleteSpec);

        Field field = TicketClientImpl.class.getDeclaredField("restClient");
        field.setAccessible(true);
        field.set(ticketClient, restClient);
    }

    private TicketDto ticketDeEjemplo() {
        return new TicketDto(UUID.randomUUID(), "Titulo", "Detalle", "Abierto", "Alta", UUID.randomUUID());
    }

    @Test
    void getAllTicketsShouldReturnListWhenSuccessful() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(getSpec.uri("/tickets")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(ticket)));

        List<TicketDto> result = ticketClient.getAllTickets();

        assertEquals(1, result.size());
        assertEquals(ticket, result.get(0));
    }

    @Test
    void getAllTicketsShouldThrowWhenServerFails() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(getSpec.uri("/tickets")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        assertThrows(RuntimeException.class, () -> ticketClient.getAllTickets());
    }

    @Test
    void searchTicketsShouldReturnListWhenSuccessful() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(getSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(ticket)));

        List<TicketDto> result = ticketClient.searchTickets("pantalla", "Abierto");

        assertEquals(1, result.size());
    }

    @Test
    void searchTicketsShouldWorkWithTitleOnly() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(getSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(ticket)));

        List<TicketDto> result = ticketClient.searchTickets("pantalla", null);

        assertEquals(1, result.size());
        assertEquals(ticket, result.get(0));
    }

    @Test
    void searchTicketsShouldWorkWithStatusOnly() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(getSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(ticket)));

        List<TicketDto> result = ticketClient.searchTickets(null, "Abierto");

        assertEquals(1, result.size());
        assertEquals(ticket, result.get(0));
    }

    @Test
    void getAllTicketsShouldThrowWhenBodyIsNull() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(getSpec.uri("/tickets")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok().build());

        assertThrows(RuntimeException.class, () -> ticketClient.getAllTickets());
    }

    @Test
    void searchTicketsShouldThrowWhenServerFails() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(getSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(RuntimeException.class, () -> ticketClient.searchTickets(null, "Abierto"));
    }

    @Test
    void searchTicketsShouldThrowWhenBodyIsNull() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(getSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok().build());

        assertThrows(RuntimeException.class, () -> ticketClient.searchTickets("pantalla", null));
    }

    @Test
    void getTicketByIdShouldReturnTicket() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();
        TicketDto ticket = ticketDeEjemplo();

        when(getSpec.uri("/tickets/{id}", id)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TicketDto.class)).thenReturn(ticket);

        TicketDto result = ticketClient.getTicketById(id);

        assertEquals(ticket, result);
    }

    @Test
    void createTicketShouldReturnCreatedTicket() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(postSpec.uri("/tickets")).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(new ResponseEntity<>(ticket, HttpStatus.CREATED));

        TicketDto result = ticketClient.createTicket(ticket);

        assertEquals(ticket, result);
    }

    @Test
    void createTicketShouldThrowWhenServerFails() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(postSpec.uri("/tickets")).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        assertThrows(RuntimeException.class, () -> ticketClient.createTicket(ticket));
    }

    @Test
    void createTicketShouldThrowWhenBodyIsNull() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        TicketDto ticket = ticketDeEjemplo();

        when(postSpec.uri("/tickets")).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(ResponseEntity.ok().build());

        assertThrows(RuntimeException.class, () -> ticketClient.createTicket(ticket));
    }

    @Test
    void updateTicketShouldReturnUpdatedTicket() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();
        TicketDto ticket = ticketDeEjemplo();

        when(putSpec.uri("/tickets/{id}", id)).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(ResponseEntity.ok(ticket));

        TicketDto result = ticketClient.updateTicket(id, ticket);

        assertEquals(ticket, result);
    }

    @Test
    void updateTicketShouldThrowWhenServerFails() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();
        TicketDto ticket = ticketDeEjemplo();

        when(putSpec.uri("/tickets/{id}", id)).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(RuntimeException.class, () -> ticketClient.updateTicket(id, ticket));
    }

    @Test
    void updateTicketShouldThrowWhenBodyIsNull() {
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();
        TicketDto ticket = ticketDeEjemplo();

        when(putSpec.uri("/tickets/{id}", id)).thenReturn(bodySpec);
        when(bodySpec.body(ticket)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(TicketDto.class)).thenReturn(ResponseEntity.ok().build());

        assertThrows(RuntimeException.class, () -> ticketClient.updateTicket(id, ticket));
    }

    @Test
    void deleteTicketShouldCompleteWhenSuccessful() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();

        when(deleteSpec.uri("/tickets/{id}", id)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.noContent().build());

        ticketClient.deleteTicket(id);
        // Si no lanza excepción, el flujo exitoso quedó cubierto.
    }

    @Test
    void deleteTicketShouldThrowWhenServerFails() {
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        UUID id = UUID.randomUUID();

        when(deleteSpec.uri("/tickets/{id}", id)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        assertThrows(RuntimeException.class, () -> ticketClient.deleteTicket(id));
    }
}