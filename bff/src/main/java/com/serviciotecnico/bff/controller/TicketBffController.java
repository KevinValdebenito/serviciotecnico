package com.serviciotecnico.bff.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.serviciotecnico.bff.dto.ClienteDTO;
import com.serviciotecnico.bff.dto.TicketDTO;
import com.serviciotecnico.bff.dto.TicketResumenDTO;

@RestController
@RequestMapping("/api/bff/tickets")
public class TicketBffController {

    private final RestClient restClient;

    @Value("${ticket.service.url}")
    private String ticketServiceUrl;

    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    @Value("${tecnico.service.url}")
    private String tecnicoServiceUrl;

    public TicketBffController(){
        this.restClient = RestClient.create();
    }

    @GetMapping("/resumen/{idTicket}")
    public ResponseEntity<TicketResumenDTO> obtenerResumenCompleto(@PathVariable UUID idTicket) {
        
        TicketResumenDTO resumen = new TicketResumenDTO();
        resumen.setIdTicket(idTicket);

        TicketDTO ticketBase;
        try {
            ticketBase = restClient.get()
                .uri(ticketServiceUrl + "/api/tickets/" + idTicket)
                .retrieve()
                .body(TicketDTO.class);
                
            resumen.setTitulo(ticketBase.getTitle());
            resumen.setEstado(ticketBase.getStatus());
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); 
        }

        try {
            String emailToSearch = (ticketBase.getClientEmail() != null) ? ticketBase.getClientEmail() : "juan.perez@example.com";
            
            ClienteDTO cliente = restClient.get()
                .uri(clienteServiceUrl + "/api/clientes/" + emailToSearch)
                .retrieve()
                .body(ClienteDTO.class);
            
            resumen.setNombreCliente(cliente.getNombreCompleto() + " (Extraído de BD)");
            resumen.setTelefonoCliente(cliente.getTelefono());
        } catch (Exception e) {
            resumen.setNombreCliente("Cliente no encontrado en BD");
            resumen.setTelefonoCliente("N/A");
        }

        if (ticketBase.getEmployeeId() != null) {
             resumen.setNombreTecnico("Técnico ID: " + ticketBase.getEmployeeId().toString().substring(0,8) + "...");
        } else {
             resumen.setNombreTecnico("Sin asignar");
        }

        return ResponseEntity.ok(resumen);
    }
}