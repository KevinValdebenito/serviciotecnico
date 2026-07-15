package com.serviciotecnico.cotizacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.cotizacion.client.TicketClient;
import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;
import com.serviciotecnico.cotizacion.dto.TicketResponse;
import com.serviciotecnico.cotizacion.entity.Cotizacion;
import com.serviciotecnico.cotizacion.exception.BusinessException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;
import com.serviciotecnico.cotizacion.repository.CotizacionRepository;

@ExtendWith(MockitoExtension.class)
class CotizacionServiceImplTest {

    @Mock
    private CotizacionRepository cotizacionRepository;

    @Mock
    private TicketClient ticketClient;

    private CotizacionServiceImpl service;

    private UUID ticketId;
    private UUID cotizacionId;
    private TicketResponse ticketAbierto;

    @BeforeEach
    void setUp() {
        service = new CotizacionServiceImpl(cotizacionRepository, ticketClient);
        ticketId = UUID.randomUUID();
        cotizacionId = UUID.randomUUID();

        ticketAbierto = new TicketResponse(
                ticketId,
                "Cambio de pantalla",
                "Pantalla quebrada",
                "ABIERTO",
                "ALTA",
                null,
                "cliente@correo.cl"
        );
    }

    @Test
    void crearDebeCalcularSubtotalIvaYTotal() {
        CotizacionRequest request = crearRequest(
                new BigDecimal("10000"),
                new BigDecimal("45000"),
                new BigDecimal("5000"),
                null
        );

        when(ticketClient.obtenerPorId(ticketId)).thenReturn(ticketAbierto);
        when(cotizacionRepository.existsByTicketIdAndEstadoIn(
                ticketId,
                List.of("PENDIENTE", "APROBADA")
        )).thenReturn(false);

        when(cotizacionRepository.save(any(Cotizacion.class)))
                .thenAnswer(invocation -> {
                    Cotizacion entity = invocation.getArgument(0);
                    entity.setId(cotizacionId);
                    entity.setFechaCreacion(LocalDateTime.now());
                    return entity;
                });

        CotizacionResponse response = service.crear(request);

        assertEquals(new BigDecimal("55000.00"), response.subtotal());
        assertEquals(new BigDecimal("50000.00"), response.neto());
        assertEquals(new BigDecimal("9500.00"), response.iva());
        assertEquals(new BigDecimal("59500.00"), response.total());
        assertEquals("PENDIENTE", response.estado());

        verify(ticketClient).obtenerPorId(ticketId);
        verify(cotizacionRepository).save(any(Cotizacion.class));
    }

    @Test
    void crearDebeRechazarDescuentoMayorAlSubtotal() {
        CotizacionRequest request = crearRequest(
                new BigDecimal("1000"),
                new BigDecimal("1000"),
                new BigDecimal("3000"),
                "PENDIENTE"
        );

        when(ticketClient.obtenerPorId(ticketId)).thenReturn(ticketAbierto);
        when(cotizacionRepository.existsByTicketIdAndEstadoIn(
                ticketId,
                List.of("PENDIENTE", "APROBADA")
        )).thenReturn(false);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.crear(request)
        );

        assertEquals(
                "El descuento no puede superar el subtotal",
                exception.getMessage()
        );
    }

    @Test
    void crearDebeRechazarTicketCerrado() {
        TicketResponse ticketCerrado = new TicketResponse(
                ticketId,
                "Ticket",
                "Descripción",
                "CERRADO",
                "BAJA",
                null,
                "cliente@correo.cl"
        );

        when(ticketClient.obtenerPorId(ticketId)).thenReturn(ticketCerrado);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.crear(crearRequest(
                        new BigDecimal("1000"),
                        new BigDecimal("1000"),
                        BigDecimal.ZERO,
                        null
                ))
        );

        assertEquals(
                "No se puede cotizar un ticket cerrado o cancelado",
                exception.getMessage()
        );
    }

    @Test
    void crearDebeRechazarOtraCotizacionActivaDelMismoTicket() {
        when(ticketClient.obtenerPorId(ticketId)).thenReturn(ticketAbierto);
        when(cotizacionRepository.existsByTicketIdAndEstadoIn(
                ticketId,
                List.of("PENDIENTE", "APROBADA")
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.crear(crearRequest(
                        new BigDecimal("1000"),
                        new BigDecimal("1000"),
                        BigDecimal.ZERO,
                        null
                ))
        );

        assertEquals(
                "El ticket ya posee una cotización pendiente o aprobada",
                exception.getMessage()
        );
    }

    @Test
    void obtenerPorIdDebeRetornarCotizacion() {
        Cotizacion entity = crearEntidad();
        when(cotizacionRepository.findById(cotizacionId))
                .thenReturn(Optional.of(entity));

        CotizacionResponse response = service.obtenerPorId(cotizacionId);

        assertEquals(cotizacionId, response.id());
        assertEquals(ticketId, response.ticketId());
        assertEquals(new BigDecimal("1190.00"), response.total());
    }

    @Test
    void obtenerPorIdDebeLanzarErrorCuandoNoExiste() {
        when(cotizacionRepository.findById(cotizacionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.obtenerPorId(cotizacionId)
        );
    }

    @Test
    void actualizarDebeRecalcularMontos() {
        Cotizacion existente = crearEntidad();

        when(cotizacionRepository.findById(cotizacionId))
                .thenReturn(Optional.of(existente));
        when(ticketClient.obtenerPorId(ticketId)).thenReturn(ticketAbierto);
        when(cotizacionRepository.save(any(Cotizacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CotizacionRequest request = crearRequest(
                new BigDecimal("2000"),
                new BigDecimal("3000"),
                new BigDecimal("500"),
                "APROBADA"
        );

        CotizacionResponse response = service.actualizar(cotizacionId, request);

        assertEquals(new BigDecimal("5000.00"), response.subtotal());
        assertEquals(new BigDecimal("4500.00"), response.neto());
        assertEquals(new BigDecimal("855.00"), response.iva());
        assertEquals(new BigDecimal("5355.00"), response.total());
        assertEquals("APROBADA", response.estado());
    }

    @Test
    void listarPorTicketDebeMapearResultados() {
        when(cotizacionRepository.findByTicketIdOrderByFechaCreacionDesc(ticketId))
                .thenReturn(List.of(crearEntidad()));

        List<CotizacionResponse> response = service.listarPorTicket(ticketId);

        assertEquals(1, response.size());
        assertEquals(ticketId, response.getFirst().ticketId());
    }

    @Test
    void eliminarDebeBorrarLaEntidad() {
        Cotizacion entity = crearEntidad();
        when(cotizacionRepository.findById(cotizacionId))
                .thenReturn(Optional.of(entity));
        doNothing().when(cotizacionRepository).delete(entity);

        service.eliminar(cotizacionId);

        verify(cotizacionRepository).delete(entity);
    }

    private CotizacionRequest crearRequest(
            BigDecimal manoObra,
            BigDecimal costoRepuestos,
            BigDecimal descuento,
            String estado
    ) {
        return new CotizacionRequest(
                ticketId,
                "Cambio de pantalla",
                manoObra,
                costoRepuestos,
                descuento,
                estado
        );
    }

    private Cotizacion crearEntidad() {
        Cotizacion entity = new Cotizacion();
        entity.setId(cotizacionId);
        entity.setTicketId(ticketId);
        entity.setDescripcion("Diagnóstico y reparación");
        entity.setManoObra(new BigDecimal("500.00"));
        entity.setCostoRepuestos(new BigDecimal("500.00"));
        entity.setDescuento(BigDecimal.ZERO.setScale(2));
        entity.setSubtotal(new BigDecimal("1000.00"));
        entity.setNeto(new BigDecimal("1000.00"));
        entity.setIva(new BigDecimal("190.00"));
        entity.setTotal(new BigDecimal("1190.00"));
        entity.setEstado("PENDIENTE");
        entity.setFechaCreacion(LocalDateTime.now());
        return entity;
    }
}
