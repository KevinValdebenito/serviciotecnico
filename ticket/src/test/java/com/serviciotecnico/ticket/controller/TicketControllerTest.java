package com.serviciotecnico.ticket.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.serviciotecnico.ticket.dto.TicketDto;
import com.serviciotecnico.ticket.service.TicketService;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {
    @Mock
    private TicketService ticketService;

    @Test
    void getAllTickets_shouldReturnOKWithAllTickets_whenNoFilters(){
        TicketController controller = new TicketController(ticketService);
        TicketDto mockTicket = new TicketDto(null, "Limpieza general", null, null, null, null, null);

        when(ticketService.getAllTickets()).thenReturn(List.of(mockTicket));

        ResponseEntity<List<TicketDto>> result = controller.getAllTickets(null, null, null, null);
        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Limpieza general", result.getBody().get(0).title());
    }

    @Test
    void getAllTickets_shouldReturnOKWithFilteredTickets_whenFiltersExist(){
        TicketController controller = new TicketController(ticketService);
        TicketDto mockTicket = new TicketDto(null, "Formateo Windows", null, null, null, null, null);

        when(ticketService.searchTickets("Windows", null, null, null)).thenReturn(List.of(mockTicket));

        ResponseEntity<List<TicketDto>> result = controller.getAllTickets("Windows", null, null, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Formateo Windows", result.getBody().get(0).title());
    }

    @Test
    void getTicketById_shouldReturnOK(){
        TicketController controller = new TicketController(ticketService);
        UUID id = UUID.randomUUID();
        TicketDto mockTicket = new TicketDto(id, null, null, null, null, null, null);

        when(ticketService.getTicketById(id)).thenReturn(mockTicket);

        ResponseEntity<TicketDto> result = controller.getTicketById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void createTicket_shouldReturnCreated() {
        TicketController controller = new TicketController(ticketService);
        TicketDto inputDto = new TicketDto(null, "Nuevo Problema", null, null, null, null, null);

        TicketDto saveDto = new TicketDto(UUID.randomUUID(), "Nuevo Problema", null, null, null, null, null);

        when(ticketService.createTicket(inputDto)).thenReturn(saveDto);

        ResponseEntity<TicketDto> result = controller.createTicket(inputDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Nuevo Problema", result.getBody().title());
    }

    @Test
    void updateTicket_shouldReturnOK() {
        TicketController controller = new TicketController(ticketService);
        UUID id = UUID.randomUUID();
        TicketDto inputDto = new TicketDto(null, "Actualizado", null, null, null, null, null);

        when(ticketService.updateTicket(id, inputDto)).thenReturn(inputDto);

        ResponseEntity<TicketDto> result = controller.updateTicket(id, inputDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Actualizado", result.getBody().title());
    }

    @Test
    void deleteTicket_shouldReturnNoContent(){
        TicketController controller = new TicketController(ticketService);
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> result = controller.deleteTicket(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(ticketService).deleteTicket(id);
    }
}
