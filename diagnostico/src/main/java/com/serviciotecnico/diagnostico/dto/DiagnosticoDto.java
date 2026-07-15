package com.serviciotecnico.diagnostico.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DiagnosticoDto(
    UUID id,
    UUID ticketId,
    UUID empleadoId,
    String descripcion,
    LocalDateTime fecha
) {}