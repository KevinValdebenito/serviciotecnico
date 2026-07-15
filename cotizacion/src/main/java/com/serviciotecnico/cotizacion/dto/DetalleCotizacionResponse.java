package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalleCotizacionResponse(
        UUID repuestoId,
        String nombreRepuesto,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal
) {
}
