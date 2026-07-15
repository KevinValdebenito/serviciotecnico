package com.serviciotecnico.bff.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.bff.dto.TicketResumenDTO;
import com.serviciotecnico.bff.services.TicketResumenService;

@RestController
@RequestMapping("/api/bff/tickets")
public class TicketBffController {

    private final TicketResumenService ticketResumenService;

    public TicketBffController(TicketResumenService ticketResumenService){
        this.ticketResumenService = ticketResumenService;
    }

    @GetMapping("/resumen/{idTicket}")
    public ResponseEntity<TicketResumenDTO> obtenerResumenCompleto(
            @PathVariable UUID idTicket,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        TicketResumenDTO resumen = ticketResumenService.obtenerResumenCompleto(idTicket, authorizationHeader);
        return ResponseEntity.ok(resumen);
    }
}