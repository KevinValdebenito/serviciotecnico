package com.serviciotecnico.empleado.dto;

import java.util.UUID;

public record TicketDto(
    UUID id,
    String name,
    String description,
    String status
) {
}