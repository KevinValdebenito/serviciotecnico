package com.serviciotecnico.notificacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.notificacion.dto.NotificacionRequest;
import com.serviciotecnico.notificacion.dto.NotificacionResponse;
import com.serviciotecnico.notificacion.service.NotificacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(
            NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> enviarNotificacion(
            @Valid @RequestBody NotificacionRequest request) {

        NotificacionResponse response =
                notificacionService.enviarNotificacion(request);

        return ResponseEntity.ok(response);
    }
}