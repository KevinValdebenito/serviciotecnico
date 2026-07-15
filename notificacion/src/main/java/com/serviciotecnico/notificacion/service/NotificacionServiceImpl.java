package com.serviciotecnico.notificacion.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.serviciotecnico.notificacion.dto.NotificacionRequest;
import com.serviciotecnico.notificacion.dto.NotificacionResponse;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificacionServiceImpl.class);

    @Override
    public NotificacionResponse enviarNotificacion(
            NotificacionRequest request) {

        UUID idNotificacion = UUID.randomUUID();

        String asunto = "Actualización de ticket";

        String mensaje = "El ticket "
                + request.ticketId()
                + " cambió su estado a "
                + request.nuevoEstado()
                + ".";

        LocalDateTime fechaEnvio = LocalDateTime.now();


        LOGGER.info(
                "Notificación simulada enviada. "
                        + "id={}, ticketId={}, destinatario={}, nuevoEstado={}",
                idNotificacion,
                request.ticketId(),
                request.destinatario(),
                request.nuevoEstado()
        );

        return new NotificacionResponse(
                idNotificacion,
                request.ticketId(),
                request.destinatario(),
                asunto,
                mensaje,
                "ENVIADA",
                fechaEnvio
        );
    }
}