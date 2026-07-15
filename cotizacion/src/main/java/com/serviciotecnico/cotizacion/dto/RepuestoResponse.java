package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RepuestoResponse(
        UUID id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock
) {
}
