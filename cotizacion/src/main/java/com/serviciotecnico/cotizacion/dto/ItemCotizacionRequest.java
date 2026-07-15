package com.serviciotecnico.cotizacion.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemCotizacionRequest(
        @NotNull(message = "El repuestoId es obligatorio")
        UUID repuestoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad
) {
}
