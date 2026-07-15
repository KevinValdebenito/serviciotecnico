package com.serviciotecnico.cotizacion.dto;

import java.util.UUID;

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
