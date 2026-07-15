package com.serviciotecnico.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.bff.dto.TicketResumenDTO;
import com.serviciotecnico.bff.services.TicketResumenService;

@ExtendWith(MockitoExtension.class)
class TicketBffControllerTest {

    private static final String AUTH_HEADER = "Bearer test-token";

    @Mock
    private TicketResumenService ticketResumenService;

    @InjectMocks
    private TicketBffController controller;

    @Test
    void shouldDelegateToServiceWhenResumenIsRequested() {
        UUID idTicket = UUID.randomUUID();
        // Usamos el constructor del record, pasando null a los campos que no validamos en este test
        TicketResumenDTO resumen = new TicketResumenDTO(idTicket, "Pantalla rota", null, null, null, null);

        when(ticketResumenService.obtenerResumenCompleto(idTicket, AUTH_HEADER)).thenReturn(resumen);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket, AUTH_HEADER).getBody();

        assertNotNull(result);
        assertEquals(idTicket, result.idTicket()); // Método de acceso sin "get"
        assertEquals("Pantalla rota", result.titulo());
        verify(ticketResumenService).obtenerResumenCompleto(idTicket, AUTH_HEADER);
    }

    @Test
    void shouldReturnServiceResponseAsOk() {
        UUID idTicket = UUID.randomUUID();

        TicketResumenDTO resumen = new TicketResumenDTO(idTicket, null, null, "Cliente Default", null, null);

        when(ticketResumenService.obtenerResumenCompleto(idTicket, AUTH_HEADER)).thenReturn(resumen);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket, AUTH_HEADER).getBody();

        assertNotNull(result);
        assertEquals("Cliente Default", result.nombreCliente());
    }
}