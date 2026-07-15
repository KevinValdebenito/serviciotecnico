package com.serviciotecnico.cotizacion.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.cotizacion.entity.Cotizacion;

/**
 * Acceso a la base de datos propia del microservicio de cotizaciones.
 */
@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, UUID> {

    List<Cotizacion> findByTicketIdOrderByFechaCreacionDesc(UUID ticketId);

    boolean existsByTicketIdAndEstadoIn(UUID ticketId, List<String> estados);
}
