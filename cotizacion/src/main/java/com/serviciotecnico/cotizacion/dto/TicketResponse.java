package com.serviciotecnico.cotizacion.dto;

import java.util.UUID;

/**
 * DTO con los datos recibidos desde el microservicio de tickets.
 */
public record TicketResponse(
        UUID id,
        String title,
        String description,
        String status,
        String priority,
        UUID employeeId,
        String clientEmail
) {
}
