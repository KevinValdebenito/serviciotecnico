package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta JSON entregada por el microservicio.
 */
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
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
