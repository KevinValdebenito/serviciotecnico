package com.serviciotecnico.bff.dto;

import java.util.UUID;

public record TicketResumenDTO(
    UUID idTicket,
    String titulo,
    String estado,
    String nombreCliente,
    String telefonoCliente,
    String nombreTecnico
) {}
