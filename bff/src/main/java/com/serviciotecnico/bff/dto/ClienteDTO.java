package com.serviciotecnico.bff.dto;

public record ClienteDTO(
    String email,
    String rut,
    String nombreCompleto,
    String telefono,
    String direccion
) {}
