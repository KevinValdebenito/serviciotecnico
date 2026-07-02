/**
 * Servicio encargado de construir un resumen completo de un ticket.
 *
 * <p>Consulta información desde los microservicios de tickets, clientes
 * y empleados para entregar una respuesta consolidada al frontend.</p>
 */
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
        TicketResumenDTO resumen = new TicketResumenDTO();
        resumen.setIdTicket(idTicket);

        TicketDTO ticketBase;
        try {
            ticketBase = restClient.get()
                .uri(ticketServiceUrl + "/api/tickets/" + idTicket)
                .retrieve()
                .body(TicketDTO.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado con id: " + idTicket);
        }

        resumen.setTitulo(ticketBase.getTitle());
        resumen.setEstado(ticketBase.getStatus());

        try {
            String emailToSearch = (ticketBase.getClientEmail() != null) ? ticketBase.getClientEmail() : "juan.perez@example.com";

            ClienteDTO cliente = restClient.get()
                .uri(clienteServiceUrl + "/api/clientes/" + emailToSearch)
                .retrieve()
                .body(ClienteDTO.class);

            resumen.setNombreCliente(cliente.getNombreCompleto() + " (Extraído de BD)");
            resumen.setTelefonoCliente(cliente.getTelefono());
        } catch (Exception exception) {
            resumen.setNombreCliente("Cliente no encontrado en BD");
            resumen.setTelefonoCliente("N/A");
        }

        if (ticketBase.getEmployeeId() != null) {
            try {
                EmpleadoDTO empleado = restClient.get()
                    .uri(tecnicoServiceUrl + "/api/empleados/" + ticketBase.getEmployeeId())
                    .retrieve()
                    .body(EmpleadoDTO.class);

                resumen.setNombreTecnico(empleado.getUsername());
            } catch (Exception exception) {
                resumen.setNombreTecnico("Técnico no encontrado en BD");
            }
        } else {
            resumen.setNombreTecnico("Técnico no asignado");
        }

        return resumen;
    }
}