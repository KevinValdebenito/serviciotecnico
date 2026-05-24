package com.serviciotecnico.ticket.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.ticket.dto.TicketDto;
import com.serviciotecnico.ticket.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false) UUID empleadoId) {

        boolean hasFilters = (titulo != null && !titulo.isBlank())
                || (estado != null && !estado.isBlank())
                || (prioridad != null && !prioridad.isBlank())
                || empleadoId != null;

        List<TicketDto> tickets = hasFilters
                ? ticketService.searchTickets(titulo, estado, prioridad, empleadoId)
                : ticketService.getAllTickets();

        return ResponseEntity.ok(tickets);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable UUID id) {
        TicketDto ticketDto = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
        TicketDto createdTicket = ticketService.createTicket(ticketDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable UUID id, @RequestBody TicketDto ticketDto) {
        TicketDto updatedTicket = ticketService.updateTicket(id, ticketDto);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
