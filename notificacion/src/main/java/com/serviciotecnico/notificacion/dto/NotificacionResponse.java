package com.serviciotecnico.notificacion.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacionResponse(

        UUID idNotificacion,
        UUID ticketId,
        String destinatario,
        String asunto,
        String mensaje,
        String estadoEnvio,
        LocalDateTime fechaEnvio

) {
}