/**
 * Implementación del servicio de tickets.
 *
 * <p>Contiene la lógica para consultar, crear, actualizar, eliminar
 * y filtrar tickets dentro del sistema.</p>
 */
package com.serviciotecnico.ticket.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.ticket.dto.TicketDto;

public interface TicketService {
    List<TicketDto> getAllTickets();

    List<TicketDto> searchTickets(String title, String status, String priority, UUID employeeId);

    TicketDto getTicketById(UUID id);

    TicketDto createTicket(TicketDto ticketDto);

    TicketDto updateTicket(UUID id, TicketDto ticketDto);

    void deleteTicket(UUID id);
}