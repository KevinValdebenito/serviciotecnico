package com.serviciotecnico.cotizacion.exception;

import java.time.LocalDateTime;

/**
 * Estructura uniforme para las respuestas de error.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
