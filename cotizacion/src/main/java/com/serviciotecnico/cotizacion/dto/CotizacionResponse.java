package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CotizacionResponse(
        UUID id,
        UUID ticketId,
        String descripcion,
        BigDecimal manoObra,
        BigDecimal costoRepuestos,
        BigDecimal descuento,
        BigDecimal subtotal,
        BigDecimal neto,
        BigDecimal iva,
        BigDecimal total,
        String estado,
        List<DetalleCotizacionResponse> repuestos,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
