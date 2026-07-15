package com.serviciotecnico.cotizacion.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;

public interface CotizacionService {

    List<CotizacionResponse> listarTodas();

    CotizacionResponse obtenerPorId(UUID id);

    List<CotizacionResponse> listarPorTicket(UUID ticketId);

    CotizacionResponse crear(CotizacionRequest request, String authorization);

    CotizacionResponse actualizar(UUID id, CotizacionRequest request, String authorization);

    CotizacionResponse aprobar(UUID id, String authorization);

    CotizacionResponse rechazar(UUID id);

    void eliminar(UUID id);
}
