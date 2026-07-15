package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para crear o actualizar una cotización.
 */
public record CotizacionRequest(

        @NotNull(message = "El ticketId es obligatorio")
        UUID ticketId,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String descripcion,

        @NotNull(message = "La mano de obra es obligatoria")
        @DecimalMin(value = "0.00", message = "La mano de obra no puede ser negativa")
        BigDecimal manoObra,

        @NotNull(message = "El costo de repuestos es obligatorio")
        @DecimalMin(value = "0.00", message = "El costo de repuestos no puede ser negativo")
        BigDecimal costoRepuestos,

        @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
        BigDecimal descuento,

        @Pattern(
                regexp = "(?i)PENDIENTE|APROBADA|RECHAZADA|VENCIDA",
                message = "El estado debe ser PENDIENTE, APROBADA, RECHAZADA o VENCIDA"
        )
        String estado
) {
}
