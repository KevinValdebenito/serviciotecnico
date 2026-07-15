package com.serviciotecnico.cotizacion.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CotizacionRequest(
        @NotNull(message = "El ticketId es obligatorio")
        UUID ticketId,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String descripcion,

        @NotNull(message = "La mano de obra es obligatoria")
        @DecimalMin(value = "0.00", message = "La mano de obra no puede ser negativa")
        BigDecimal manoObra,

        @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
        BigDecimal descuento,

        @NotEmpty(message = "Debe incluir al menos un repuesto")
        List<@Valid ItemCotizacionRequest> repuestos
) {
}
