package com.serviciotecnico.cotizacion.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serviciotecnico.cotizacion.entity.Cotizacion;

public interface CotizacionRepository extends JpaRepository<Cotizacion, UUID> {

    List<Cotizacion> findByTicketIdOrderByFechaCreacionDesc(UUID ticketId);

    boolean existsByTicketIdAndEstadoIn(
            UUID ticketId,
            Collection<String> estados
    );

    boolean existsByTicketIdAndEstadoInAndIdNot(
            UUID ticketId,
            Collection<String> estados,
            UUID id
    );
}
