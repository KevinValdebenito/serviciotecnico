package com.serviciotecnico.cliente.dto;

public record ClienteDto(
    String email,
    String rut,
    String nombreCompleto,
    String telefono,
    String direccion
) {}