package com.serviciotecnico.cotizacion.client;

import java.util.UUID;

import com.serviciotecnico.cotizacion.dto.TicketResponse;

public interface TicketClient {

    TicketResponse obtenerPorId(UUID ticketId, String authorization);
}
