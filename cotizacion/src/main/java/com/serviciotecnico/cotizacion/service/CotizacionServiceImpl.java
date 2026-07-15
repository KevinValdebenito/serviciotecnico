package com.serviciotecnico.cotizacion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serviciotecnico.cotizacion.client.RepuestoClient;
import com.serviciotecnico.cotizacion.client.TicketClient;
import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;
import com.serviciotecnico.cotizacion.dto.DetalleCotizacionResponse;
import com.serviciotecnico.cotizacion.dto.ItemCotizacionRequest;
import com.serviciotecnico.cotizacion.dto.RepuestoResponse;
import com.serviciotecnico.cotizacion.dto.TicketResponse;
import com.serviciotecnico.cotizacion.entity.Cotizacion;
import com.serviciotecnico.cotizacion.entity.CotizacionDetalle;
import com.serviciotecnico.cotizacion.exception.BusinessException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;
import com.serviciotecnico.cotizacion.exception.StockException;
import com.serviciotecnico.cotizacion.repository.CotizacionRepository;

@Service
public class CotizacionServiceImpl implements CotizacionService {

    private static final Logger log = LoggerFactory.getLogger(CotizacionServiceImpl.class);
    private static final BigDecimal IVA = new BigDecimal("0.19");
    private static final Set<String> ESTADOS_ACTIVOS = Set.of("PENDIENTE", "APROBADA");

    private final CotizacionRepository cotizacionRepository;
    private final RepuestoClient repuestoClient;
    private final TicketClient ticketClient;

    public CotizacionServiceImpl(
            CotizacionRepository cotizacionRepository,
            RepuestoClient repuestoClient,
            TicketClient ticketClient
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.repuestoClient = repuestoClient;
        this.ticketClient = ticketClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CotizacionResponse> listarTodas() {
        return cotizacionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CotizacionResponse obtenerPorId(UUID id) {
        return convertirAResponse(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CotizacionResponse> listarPorTicket(UUID ticketId) {
        return cotizacionRepository.findByTicketIdOrderByFechaCreacionDesc(ticketId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional
    public CotizacionResponse crear(
            CotizacionRequest request,
            String authorization
    ) {
        TicketResponse ticket = ticketClient.obtenerPorId(
                request.ticketId(),
                authorization
        );
        validarTicketCotizable(ticket);

        if (cotizacionRepository.existsByTicketIdAndEstadoIn(
                request.ticketId(),
                ESTADOS_ACTIVOS
        )) {
            throw new BusinessException(
                    "El ticket ya posee una cotización pendiente o aprobada");
        }

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setEstado("PENDIENTE");
        aplicarDatos(cotizacion, request, authorization);

        Cotizacion guardada = cotizacionRepository.saveAndFlush(cotizacion);
        log.info("Cotización {} creada con total {}", guardada.getId(), guardada.getTotal());
        return convertirAResponse(guardada);
    }

    @Override
    @Transactional
    public CotizacionResponse actualizar(
            UUID id,
            CotizacionRequest request,
            String authorization
    ) {
        Cotizacion cotizacion = buscarEntidad(id);
        validarPendiente(cotizacion, "actualizar");

        TicketResponse ticket = ticketClient.obtenerPorId(
                request.ticketId(),
                authorization
        );
        validarTicketCotizable(ticket);

        if (cotizacionRepository.existsByTicketIdAndEstadoInAndIdNot(
                request.ticketId(),
                ESTADOS_ACTIVOS,
                id
        )) {
            throw new BusinessException(
                    "El ticket ya posee otra cotización pendiente o aprobada");
        }

        aplicarDatos(cotizacion, request, authorization);

        Cotizacion actualizada = cotizacionRepository.saveAndFlush(cotizacion);
        log.info("Cotización {} actualizada", id);
        return convertirAResponse(actualizada);
    }

    @Override
@Transactional
public CotizacionResponse aprobar(UUID id, String authorization) {
    Cotizacion cotizacion = buscarEntidad(id);
    validarPendiente(cotizacion, "aprobar");

    validarStockActual(cotizacion, authorization);

    List<CotizacionDetalle> descontados = new ArrayList<>();

    try {
        for (CotizacionDetalle detalle : cotizacion.getDetalles()) {
            repuestoClient.reducirStock(
                    detalle.getRepuestoId(),
                    detalle.getCantidad(),
                    authorization
            );

            descontados.add(detalle);
        }

        cotizacion.setEstado("APROBADA");

        Cotizacion aprobada =
                cotizacionRepository.saveAndFlush(cotizacion);

        log.info(
                "Cotización {} aprobada y stock descontado",
                id
        );

        return convertirAResponse(aprobada);

    } catch (RuntimeException ex) {
        compensarStock(descontados, authorization);
        throw ex;
    }
}

    @Override
    @Transactional
    public CotizacionResponse rechazar(UUID id) {
        Cotizacion cotizacion = buscarEntidad(id);
        validarPendiente(cotizacion, "rechazar");
        cotizacion.setEstado("RECHAZADA");

        Cotizacion rechazada = cotizacionRepository.saveAndFlush(cotizacion);
        log.info("Cotización {} rechazada", id);
        return convertirAResponse(rechazada);
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Cotizacion cotizacion = buscarEntidad(id);

        if ("APROBADA".equals(cotizacion.getEstado())) {
            throw new BusinessException(
                    "No se puede eliminar una cotización aprobada");
        }

        cotizacionRepository.delete(cotizacion);
        log.info("Cotización {} eliminada", id);
    }

    private void aplicarDatos(
            Cotizacion cotizacion,
            CotizacionRequest request,
            String authorization
    ) {
        List<CotizacionDetalle> detalles = construirDetalles(
                request.repuestos(),
                authorization
        );

        cotizacion.setTicketId(request.ticketId());
        cotizacion.setDescripcion(request.descripcion().trim());
        cotizacion.setManoObra(escalar(request.manoObra()));
        cotizacion.setDescuento(escalar(
                request.descuento() == null ? BigDecimal.ZERO : request.descuento()
        ));
        cotizacion.reemplazarDetalles(detalles);

        calcularMontos(cotizacion);
    }

    private List<CotizacionDetalle> construirDetalles(
            List<ItemCotizacionRequest> items,
            String authorization
    ) {
        Set<UUID> ids = new HashSet<>();
        List<CotizacionDetalle> detalles = new ArrayList<>();

        for (ItemCotizacionRequest item : items) {
            if (!ids.add(item.repuestoId())) {
                throw new BusinessException(
                        "No repitas el mismo repuesto dentro de la cotización");
            }

            RepuestoResponse repuesto = repuestoClient.obtenerPorId(
                    item.repuestoId(),
                    authorization
            );

            validarRepuesto(repuesto, item.cantidad());

            BigDecimal precio = escalar(repuesto.precio());
            BigDecimal subtotal = precio
                    .multiply(BigDecimal.valueOf(item.cantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            detalles.add(new CotizacionDetalle(
                    repuesto.id(),
                    repuesto.nombre(),
                    precio,
                    item.cantidad(),
                    subtotal
            ));
        }

        return detalles;
    }

    private void validarRepuesto(RepuestoResponse repuesto, Integer cantidad) {
        if (repuesto.precio() == null || repuesto.precio().signum() < 0) {
            throw new BusinessException(
                    "El repuesto " + repuesto.id() + " tiene un precio inválido");
        }

        if (repuesto.stock() == null || repuesto.stock() < cantidad) {
            throw new StockException(
                    "Stock insuficiente para '" + repuesto.nombre()
                            + "'. Disponible: " + repuesto.stock()
                            + ", solicitado: " + cantidad);
        }
    }

    private void validarStockActual(Cotizacion cotizacion, String authorization) {
        for (CotizacionDetalle detalle : cotizacion.getDetalles()) {
            RepuestoResponse repuesto = repuestoClient.obtenerPorId(
                    detalle.getRepuestoId(),
                    authorization
            );
            validarRepuesto(repuesto, detalle.getCantidad());
        }
    }

    private void compensarStock(
            List<CotizacionDetalle> descontados,
            String authorization
    ) {
        for (int i = descontados.size() - 1; i >= 0; i--) {
            CotizacionDetalle detalle = descontados.get(i);
            try {
                repuestoClient.aumentarStock(
                        detalle.getRepuestoId(),
                        detalle.getCantidad(),
                        authorization
                );
            } catch (RuntimeException compensationError) {
                log.error(
                        "No se pudo compensar el stock del repuesto {}",
                        detalle.getRepuestoId(),
                        compensationError
                );
            }
        }
    }

    private void calcularMontos(Cotizacion cotizacion) {
        BigDecimal costoRepuestos = cotizacion.getDetalles()
                .stream()
                .map(CotizacionDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal subtotal = cotizacion.getManoObra()
                .add(costoRepuestos)
                .setScale(2, RoundingMode.HALF_UP);

        if (cotizacion.getDescuento().compareTo(subtotal) > 0) {
            throw new BusinessException(
                    "El descuento no puede superar el subtotal");
        }

        BigDecimal neto = subtotal
                .subtract(cotizacion.getDescuento())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = neto
                .multiply(IVA)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = neto
                .add(iva)
                .setScale(2, RoundingMode.HALF_UP);

        cotizacion.setCostoRepuestos(costoRepuestos);
        cotizacion.setSubtotal(subtotal);
        cotizacion.setNeto(neto);
        cotizacion.setIva(iva);
        cotizacion.setTotal(total);
    }

    private BigDecimal escalar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarTicketCotizable(TicketResponse ticket) {
        String estado = ticket.status() == null
                ? ""
                : ticket.status().trim().toUpperCase();

        if ("CERRADO".equals(estado) || "CANCELADO".equals(estado)) {
            throw new BusinessException(
                    "No se puede cotizar un ticket cerrado o cancelado");
        }
    }

    private void validarPendiente(Cotizacion cotizacion, String operacion) {
        if (!"PENDIENTE".equals(cotizacion.getEstado())) {
            throw new BusinessException(
                    "Solo se puede " + operacion + " una cotización PENDIENTE");
        }
    }

    private Cotizacion buscarEntidad(UUID id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una cotización con id: " + id));
    }

    private CotizacionResponse convertirAResponse(Cotizacion cotizacion) {
        List<DetalleCotizacionResponse> repuestos = cotizacion.getDetalles()
                .stream()
                .map(detalle -> new DetalleCotizacionResponse(
                        detalle.getRepuestoId(),
                        detalle.getNombreRepuesto(),
                        detalle.getPrecioUnitario(),
                        detalle.getCantidad(),
                        detalle.getSubtotal()
                ))
                .toList();

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
                repuestos,
                cotizacion.getFechaCreacion(),
                cotizacion.getFechaActualizacion()
        );
    }
}
