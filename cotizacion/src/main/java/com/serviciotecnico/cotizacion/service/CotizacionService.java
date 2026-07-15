package com.serviciotecnico.cotizacion.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;

/**
 * Operaciones y reglas de negocio del microservicio de cotizaciones.
 */
public interface CotizacionService {

    /**
     * Lista todas las cotizaciones.
     *
     * @return cotizaciones registradas
     */
    List<CotizacionResponse> listarTodas();

    /**
     * Busca una cotización por su identificador.
     *
     * @param id identificador de la cotización
     * @return cotización encontrada
     */
    CotizacionResponse obtenerPorId(UUID id);

    /**
     * Lista las cotizaciones asociadas a un ticket.
     *
     * @param ticketId identificador del ticket
     * @return cotizaciones del ticket
     */
    List<CotizacionResponse> listarPorTicket(UUID ticketId);

    /**
     * Crea una cotización, valida el ticket y calcula los montos.
     *
     * @param request datos de entrada
     * @return cotización creada
     */
    CotizacionResponse crear(CotizacionRequest request);

    /**
     * Actualiza una cotización y vuelve a calcular sus montos.
     *
     * @param id identificador de la cotización
     * @param request datos de entrada
     * @return cotización actualizada
     */
    CotizacionResponse actualizar(UUID id, CotizacionRequest request);

    /**
     * Elimina una cotización.
     *
     * @param id identificador de la cotización
     */
    void eliminar(UUID id);
}
