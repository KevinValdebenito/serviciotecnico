package com.serviciotecnico.empleado.client;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.empleado.dto.TicketDto;

public interface TicketClient {
    List<TicketDto> getAllTickets();
    List<TicketDto> searchTickets(String title, String status);
    TicketDto getTicketById(UUID id);
    TicketDto createTicket(TicketDto ticketDto);
    TicketDto updateTicket(UUID id, TicketDto ticketDto);
    void deleteTicket(UUID id);
}
