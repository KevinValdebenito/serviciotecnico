package com.serviciotecnico.bff.dto;

import java.util.UUID;

public record EmpleadoDTO(
    UUID id,
    String username,
    String email,
    String role
) {}
