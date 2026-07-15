package com.serviciotecnico.notificacion.service;

import com.serviciotecnico.notificacion.dto.NotificacionRequest;
import com.serviciotecnico.notificacion.dto.NotificacionResponse;

public interface NotificacionService {

    NotificacionResponse enviarNotificacion(NotificacionRequest request);
}