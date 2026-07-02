package com.serviciotecnico.ticket.dto;

import java.util.UUID;

public record TicketDto(
    UUID id,
    String title,
    String description,
    String status,
    String priority,
    UUID employeeId,
    String clientEmail
) {}