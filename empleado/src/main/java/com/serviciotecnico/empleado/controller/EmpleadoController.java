package com.serviciotecnico.empleado.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.empleado.client.TicketClient;
import com.serviciotecnico.empleado.dto.ApiResponse;
import com.serviciotecnico.empleado.dto.TicketDto;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    final TicketClient ticketClient;

    public EmpleadoController(TicketClient ticketClient) {
        this.ticketClient = ticketClient;
    }

    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<List<TicketDto>>> getAllTickets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        List<TicketDto> tickets;
        boolean hasFilters = (name != null && !name.isBlank()) || (status != null && !status.isBlank());

        if (hasFilters) {
            tickets = ticketClient.searchTickets(name, status);
        } else {
            tickets = ticketClient.getAllTickets();
        }

        ApiResponse<List<TicketDto>> response = new ApiResponse<>(true, tickets);
        return ResponseEntity.ok(response);
    }
}
