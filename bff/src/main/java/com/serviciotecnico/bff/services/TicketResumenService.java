package com.serviciotecnico.bff.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.bff.dto.ClienteDTO;
import com.serviciotecnico.bff.dto.EmpleadoDTO;
import com.serviciotecnico.bff.dto.TicketDTO;
import com.serviciotecnico.bff.dto.TicketResumenDTO;

@Service
public class TicketResumenService {

    private final RestClient restClient;
    private final String ticketServiceUrl;
    private final String clienteServiceUrl;
    private final String tecnicoServiceUrl;

    public TicketResumenService(
        RestClient restClient,
        @Value("${ticket.service.url}") String ticketServiceUrl,
        @Value("${cliente.service.url}") String clienteServiceUrl,
        @Value("${tecnico.service.url}") String tecnicoServiceUrl
    ) {
        this.restClient = restClient;
        this.ticketServiceUrl = ticketServiceUrl;
        this.clienteServiceUrl = clienteServiceUrl;
        this.tecnicoServiceUrl = tecnicoServiceUrl;
    }

    public TicketResumenDTO obtenerResumenCompleto(UUID idTicket) {
        TicketDTO ticketBase;
        try {
            ticketBase = restClient.get()
                .uri(ticketServiceUrl + "/api/tickets/" + idTicket)
                .retrieve()
                .body(TicketDTO.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado con id: " + idTicket);
        }

        String titulo = ticketBase.title();
        String estado = ticketBase.status();

        String nombreCliente;
        String telefonoCliente;
        try {
            String emailToSearch = (ticketBase.clientEmail() != null) ? ticketBase.clientEmail() : "juan.perez@example.com";

            ClienteDTO cliente = restClient.get()
                .uri(clienteServiceUrl + "/api/clientes/" + emailToSearch)
                .retrieve()
                .body(ClienteDTO.class);

            nombreCliente = cliente.nombreCompleto() + " (Extraído de BD)";
            telefonoCliente = cliente.telefono();
        } catch (Exception exception) {
            nombreCliente = "Cliente no encontrado en BD";
            telefonoCliente = "N/A";
        }

        String nombreTecnico;
        if (ticketBase.employeeId() != null) {
            try {
                EmpleadoDTO empleado = restClient.get()
                    .uri(tecnicoServiceUrl + "/api/empleados/" + ticketBase.employeeId())
                    .retrieve()
                    .body(EmpleadoDTO.class);

                nombreTecnico = empleado.username();
            } catch (Exception exception) {
                nombreTecnico = "Técnico no encontrado en BD";
            }
        } else {
            nombreTecnico = "Técnico no asignado";
        }

        return new TicketResumenDTO(
            idTicket,
            titulo,
            estado,
            nombreCliente,
            telefonoCliente,
            nombreTecnico
        );
    }
}