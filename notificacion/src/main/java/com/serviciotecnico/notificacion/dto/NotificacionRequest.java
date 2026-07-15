package com.serviciotecnico.notificacion.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacionRequest(

        @NotNull(message = "El ticketId es obligatorio")
        UUID ticketId,

        @NotBlank(message = "El destinatario es obligatorio")
        @Email(message = "El destinatario debe ser un correo válido")
        String destinatario,

        @NotBlank(message = "El nuevo estado del ticket es obligatorio")
        String nuevoEstado

) {
}