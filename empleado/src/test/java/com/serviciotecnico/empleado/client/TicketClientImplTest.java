package com.serviciotecnico.empleado.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.net.URI;
import java.util.UUID;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import com.serviciotecnico.empleado.dto.TicketDto;

class TicketClientImplTest {

    private static final ParameterizedTypeReference<List<TicketDto>> TICKET_LIST_TYPE = new ParameterizedTypeReference<List<TicketDto>>() {};

    private RestClient restClient;

    private TicketClientImpl ticketClient;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        ticketClient = new TicketClientImpl("http://localhost:8082");
        org.springframework.test.util.ReflectionTestUtils.setField(ticketClient, "restClient", restClient);
    }

    @Test
    void getAllTicketsShouldReturnTicketsWhenResponseIsOk() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Problema", "Detalle", "Abierto", "Alta", UUID.randomUUID()));

        when(restClient.get().uri("/tickets").retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.getAllTickets();

        assertEquals(1, result.size());
        assertEquals("Problema", result.get(0).title());
    }

    @Test
    void getAllTicketsShouldThrowWhenResponseIsNotSuccessful() {
        when(restClient.get().uri("/tickets").retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.getAllTickets());

        assertEquals("Failed to fetch tickets: 500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @Test
    void searchTicketsShouldReturnTicketsWhenFiltersArePresent() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID()));

        when(restClient.get().uri(anyFunction()).retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.searchTickets("pantalla", "Abierto");

        assertEquals(1, result.size());
        assertEquals("Pantalla", result.get(0).title());
    }

    @Test
    void searchTicketsShouldReturnTicketsWhenOnlyTitleIsPresent() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID()));

        when(restClient.get().uri(anyFunction()).retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.searchTickets("pantalla", null);

        assertEquals(1, result.size());
    }

    @Test
    void searchTicketsShouldReturnTicketsWhenOnlyStatusIsPresent() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID()));

        when(restClient.get().uri(anyFunction()).retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.searchTickets(null, "Abierto");

        assertEquals(1, result.size());
    }

    @Test
    void searchTicketsShouldReturnTicketsWhenTitleIsBlank() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID()));

        when(restClient.get().uri(anyFunction()).retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.searchTickets("   ", "Abierto");

        assertEquals(1, result.size());
    }

    @Test
    void searchTicketsShouldReturnTicketsWhenStatusIsBlank() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID()));

        when(restClient.get().uri(anyFunction()).retrieve().toEntity(TICKET_LIST_TYPE))
            .thenReturn(ResponseEntity.ok(tickets));

        List<TicketDto> result = ticketClient.searchTickets("pantalla", "   ");

        assertEquals(1, result.size());
    }

    @Test
    void getTicketByIdShouldReturnTicket() {
        UUID id = UUID.randomUUID();
        TicketDto ticket = new TicketDto(id, "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID());

        when(restClient.get().uri("/tickets/{id}", id).retrieve().body(TicketDto.class)).thenReturn(ticket);

        TicketDto result = ticketClient.getTicketById(id);

        assertEquals(id, result.id());
        assertEquals("Pantalla", result.title());
    }

    @Test
    void createTicketShouldReturnCreatedTicket() {
        TicketDto request = new TicketDto(null, "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID());
        TicketDto created = new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Media", request.employeeId());

        when(restClient.post().uri("/tickets").body(request).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(created));

        TicketDto result = ticketClient.createTicket(request);

        assertEquals(created.id(), result.id());
    }

    @Test
    void createTicketShouldThrowWhenBodyIsNull() {
        TicketDto request = new TicketDto(null, "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID());

        when(restClient.post().uri("/tickets").body(request).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.createTicket(request));

        assertEquals("Failed to create ticket: 201 CREATED", exception.getMessage());
    }

    @Test
    void createTicketShouldThrowWhenStatusIsNotSuccessful() {
        TicketDto request = new TicketDto(null, "Pantalla", "Detalle", "Abierto", "Media", UUID.randomUUID());

        when(restClient.post().uri("/tickets").body(request).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.createTicket(request));

        assertEquals("Failed to create ticket: 400 BAD_REQUEST", exception.getMessage());
    }

    @Test
    void updateTicketShouldReturnUpdatedTicket() {
        UUID id = UUID.randomUUID();
        TicketDto update = new TicketDto(id, "Pantalla", "Detalle", "Cerrado", "Media", UUID.randomUUID());

        when(restClient.put().uri("/tickets/{id}", id).body(update).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.ok(update));

        TicketDto result = ticketClient.updateTicket(id, update);

        assertEquals("Cerrado", result.status());
    }

    @Test
    void updateTicketShouldThrowWhenBodyIsNull() {
        UUID id = UUID.randomUUID();
        TicketDto update = new TicketDto(id, "Pantalla", "Detalle", "Cerrado", "Media", UUID.randomUUID());

        when(restClient.put().uri("/tickets/{id}", id).body(update).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.updateTicket(id, update));

        assertEquals("Failed to update ticket: 200 OK", exception.getMessage());
    }

    @Test
    void updateTicketShouldThrowWhenStatusIsNotSuccessful() {
        UUID id = UUID.randomUUID();
        TicketDto update = new TicketDto(id, "Pantalla", "Detalle", "Cerrado", "Media", UUID.randomUUID());

        when(restClient.put().uri("/tickets/{id}", id).body(update).retrieve().toEntity(TicketDto.class))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.updateTicket(id, update));

        assertEquals("Failed to update ticket: 400 BAD_REQUEST", exception.getMessage());
    }

    @Test
    void deleteTicketShouldNotThrowWhenResponseIsSuccessful() {
        UUID id = UUID.randomUUID();

        when(restClient.delete().uri("/tickets/{id}", id).retrieve().toBodilessEntity())
            .thenReturn(ResponseEntity.noContent().build());

        ticketClient.deleteTicket(id);
    }

    @Test
    void deleteTicketShouldThrowWhenResponseIsNotSuccessful() {
        UUID id = UUID.randomUUID();

        when(restClient.delete().uri("/tickets/{id}", id).retrieve().toBodilessEntity())
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketClient.deleteTicket(id));

        assertEquals("Failed to delete ticket: 400 BAD_REQUEST", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    private static Function<UriBuilder, URI> anyFunction() {
        return (Function<UriBuilder, URI>) any(Function.class);
    }
}