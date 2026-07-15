package com.serviciotecnico.cotizacion.client;

import java.util.UUID;

import com.serviciotecnico.cotizacion.dto.TicketResponse;

/**
 * Contrato para la comunicación con el microservicio de tickets.
 */
public interface TicketClient {

    /**
     * Obtiene un ticket remoto por su identificador.
     *
     * @param ticketId identificador del ticket
     * @return información del ticket
     */
    TicketResponse obtenerPorId(UUID ticketId);
}
