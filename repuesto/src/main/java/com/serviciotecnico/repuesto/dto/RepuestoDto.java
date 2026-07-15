package com.serviciotecnico.repuesto.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RepuestoDto(
    UUID id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock
) {}