package com.serviciotecnico.cotizacion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serviciotecnico.cotizacion.client.TicketClient;
import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;
import com.serviciotecnico.cotizacion.dto.TicketResponse;
import com.serviciotecnico.cotizacion.entity.Cotizacion;
import com.serviciotecnico.cotizacion.exception.BusinessException;
import com.serviciotecnico.cotizacion.exception.PersistenceOperationException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;
import com.serviciotecnico.cotizacion.repository.CotizacionRepository;

/**
 * Implementación de las reglas y operaciones de cotizaciones.
 */
@Service
public class CotizacionServiceImpl implements CotizacionService {

    private static final Logger log = LoggerFactory.getLogger(CotizacionServiceImpl.class);

    private static final BigDecimal PORCENTAJE_IVA = new BigDecimal("0.19");
    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "PENDIENTE",
            "APROBADA",
            "RECHAZADA",
            "VENCIDA"
    );
    private static final List<String> ESTADOS_ACTIVOS = List.of(
            "PENDIENTE",
            "APROBADA"
    );

    private final CotizacionRepository cotizacionRepository;
    private final TicketClient ticketClient;

    public CotizacionServiceImpl(
            CotizacionRepository cotizacionRepository,
            TicketClient ticketClient
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.ticketClient = ticketClient;
    }

    /**
     * Lista todas las cotizaciones guardadas en la base de datos.
     *
     * @return cotizaciones registradas
     */
    @Override
    @Transactional(readOnly = true)
    public List<CotizacionResponse> listarTodas() {
        log.info("Listando todas las cotizaciones");

        return cotizacionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * Obtiene una cotización por id.
     *
     * @param id identificador de la cotización
     * @return cotización encontrada
     */
    @Override
    @Transactional(readOnly = true)
    public CotizacionResponse obtenerPorId(UUID id) {
        Cotizacion cotizacion = buscarEntidad(id);
        return convertirAResponse(cotizacion);
    }

    /**
     * Obtiene las cotizaciones asociadas a un ticket.
     *
     * @param ticketId identificador del ticket
     * @return lista ordenada desde la más reciente
     */
    @Override
    @Transactional(readOnly = true)
    public List<CotizacionResponse> listarPorTicket(UUID ticketId) {
        log.info("Listando cotizaciones del ticket {}", ticketId);

        return cotizacionRepository
                .findByTicketIdOrderByFechaCreacionDesc(ticketId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * Crea una cotización después de comprobar que el ticket remoto existe.
     *
     * @param request datos enviados por el cliente
     * @return cotización creada
     */
    @Override
    @Transactional
    public CotizacionResponse crear(CotizacionRequest request) {
        TicketResponse ticket = ticketClient.obtenerPorId(request.ticketId());
        validarTicketCotizable(ticket);

        if (cotizacionRepository.existsByTicketIdAndEstadoIn(
                request.ticketId(),
                ESTADOS_ACTIVOS
        )) {
            throw new BusinessException(
                    "El ticket ya posee una cotización pendiente o aprobada");
        }

        Cotizacion cotizacion = new Cotizacion();
        aplicarDatos(cotizacion, request, true);
        calcularMontos(cotizacion);

        try {
            Cotizacion guardada = cotizacionRepository.save(cotizacion);
            log.info(
                    "Cotización {} creada para ticket {} con total {}",
                    guardada.getId(),
                    guardada.getTicketId(),
                    guardada.getTotal()
            );
            return convertirAResponse(guardada);
        } catch (DataAccessException ex) {
            log.error("Error al guardar la cotización", ex);
            throw new PersistenceOperationException(
                    "No fue posible guardar la cotización",
                    ex
            );
        }
    }

    /**
     * Actualiza una cotización existente y recalcula subtotal, neto, IVA y total.
     *
     * @param id identificador de la cotización
     * @param request nuevos datos
     * @return cotización actualizada
     */
    @Override
    @Transactional
    public CotizacionResponse actualizar(UUID id, CotizacionRequest request) {
        Cotizacion existente = buscarEntidad(id);

        TicketResponse ticket = ticketClient.obtenerPorId(request.ticketId());
        validarTicketCotizable(ticket);

        aplicarDatos(existente, request, false);
        calcularMontos(existente);

        try {
            Cotizacion actualizada = cotizacionRepository.save(existente);
            log.info("Cotización {} actualizada", id);
            return convertirAResponse(actualizada);
        } catch (DataAccessException ex) {
            log.error("Error al actualizar la cotización {}", id, ex);
            throw new PersistenceOperationException(
                    "No fue posible actualizar la cotización",
                    ex
            );
        }
    }

    /**
     * Elimina una cotización existente.
     *
     * @param id identificador de la cotización
     */
    @Override
    @Transactional
    public void eliminar(UUID id) {
        Cotizacion existente = buscarEntidad(id);

        try {
            cotizacionRepository.delete(existente);
            log.info("Cotización {} eliminada", id);
        } catch (DataAccessException ex) {
            log.error("Error al eliminar la cotización {}", id, ex);
            throw new PersistenceOperationException(
                    "No fue posible eliminar la cotización",
                    ex
            );
        }
    }

    private Cotizacion buscarEntidad(UUID id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una cotización con id: " + id));
    }

    private void aplicarDatos(
            Cotizacion cotizacion,
            CotizacionRequest request,
            boolean esCreacion
    ) {
        cotizacion.setTicketId(request.ticketId());
        cotizacion.setDescripcion(request.descripcion().trim());
        cotizacion.setManoObra(request.manoObra());
        cotizacion.setCostoRepuestos(request.costoRepuestos());
        cotizacion.setDescuento(
                request.descuento() == null
                        ? BigDecimal.ZERO
                        : request.descuento()
        );

        if (request.estado() != null && !request.estado().isBlank()) {
            cotizacion.setEstado(normalizarEstado(request.estado()));
        } else if (esCreacion) {
            cotizacion.setEstado("PENDIENTE");
        }
    }

    private String normalizarEstado(String estado) {
        String normalizado = estado.trim().toUpperCase();

        if (!ESTADOS_VALIDOS.contains(normalizado)) {
            throw new BusinessException(
                    "Estado inválido. Use PENDIENTE, APROBADA, RECHAZADA o VENCIDA");
        }

        return normalizado;
    }

    private void validarTicketCotizable(TicketResponse ticket) {
        if (ticket.id() == null) {
            throw new BusinessException(
                    "La respuesta del ticket no contiene un identificador válido");
        }

        String estadoTicket = ticket.status() == null
                ? ""
                : ticket.status().trim().toUpperCase();

        if ("CERRADO".equals(estadoTicket) || "CANCELADO".equals(estadoTicket)) {
            throw new BusinessException(
                    "No se puede cotizar un ticket cerrado o cancelado");
        }
    }

    private void calcularMontos(Cotizacion cotizacion) {
        BigDecimal manoObra = escalar(cotizacion.getManoObra());
        BigDecimal costoRepuestos = escalar(cotizacion.getCostoRepuestos());
        BigDecimal descuento = escalar(cotizacion.getDescuento());

        BigDecimal subtotal = manoObra.add(costoRepuestos);

        if (descuento.compareTo(subtotal) > 0) {
            throw new BusinessException(
                    "El descuento no puede superar el subtotal");
        }

        BigDecimal neto = subtotal.subtract(descuento);
        BigDecimal iva = neto
                .multiply(PORCENTAJE_IVA)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = neto
                .add(iva)
                .setScale(2, RoundingMode.HALF_UP);

        cotizacion.setManoObra(manoObra);
        cotizacion.setCostoRepuestos(costoRepuestos);
        cotizacion.setDescuento(descuento);
        cotizacion.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        cotizacion.setNeto(neto.setScale(2, RoundingMode.HALF_UP));
        cotizacion.setIva(iva);
        cotizacion.setTotal(total);
    }

    private BigDecimal escalar(BigDecimal valor) {
        if (valor == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private CotizacionResponse convertirAResponse(Cotizacion cotizacion) {
        return new CotizacionResponse(
                cotizacion.getId(),
                cotizacion.getTicketId(),
                cotizacion.getDescripcion(),
                cotizacion.getManoObra(),
                cotizacion.getCostoRepuestos(),
                cotizacion.getDescuento(),
                cotizacion.getSubtotal(),
                cotizacion.getNeto(),
                cotizacion.getIva(),
                cotizacion.getTotal(),
                cotizacion.getEstado(),
                cotizacion.getFechaCreacion(),
                cotizacion.getFechaActualizacion()
        );
    }
}
