package com.serviciotecnico.ticket.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.ticket.dto.TicketDto;

public interface TicketService {
    List<TicketDto> getAllTickets();

    List<TicketDto> searchTickets(String titulo, String estado, String prioridad, UUID empleadoId);

    TicketDto getTicketById(UUID id);

    TicketDto createTicket(TicketDto ticketDto);

    TicketDto updateTicket(UUID id, TicketDto ticketDto);

    void deleteTicket(UUID id);
}
