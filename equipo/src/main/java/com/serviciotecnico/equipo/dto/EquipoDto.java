package com.serviciotecnico.equipo.dto;

import java.util.UUID;

public record EquipoDto(
    UUID id,
    String clienteEmail,
    String tipo,
    String marca,
    String modelo,
    String numeroSerie
) {}