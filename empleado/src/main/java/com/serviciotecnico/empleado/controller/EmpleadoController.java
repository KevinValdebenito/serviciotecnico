package com.serviciotecnico.empleado.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.empleado.client.TicketClient;
import com.serviciotecnico.empleado.dto.ApiResponse;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    final TicketClient ticketClient;
    final EmpleadoService empleadoService;

    public EmpleadoController(TicketClient ticketClient, EmpleadoService empleadoService) {
        this.ticketClient = ticketClient;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> getEmpleadoById(@PathVariable UUID id){
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
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
