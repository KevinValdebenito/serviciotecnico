package com.serviciotecnico.cotizacion.client;

import java.util.UUID;

import com.serviciotecnico.cotizacion.dto.RepuestoResponse;

public interface RepuestoClient {

    RepuestoResponse obtenerPorId(UUID repuestoId, String authorization);

    RepuestoResponse reducirStock(UUID repuestoId, Integer cantidad, String authorization);

    RepuestoResponse aumentarStock(UUID repuestoId, Integer cantidad, String authorization);
}
