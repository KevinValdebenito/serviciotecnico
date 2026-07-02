package com.serviciotecnico.bff.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public record TicketDTO(
    UUID id,
    String title,
    String description,
    String status,
    String clientEmail,
    UUID employeeId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
