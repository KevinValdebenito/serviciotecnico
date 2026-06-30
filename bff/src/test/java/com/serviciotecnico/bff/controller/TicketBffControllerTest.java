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

    @Mock
    private TicketResumenService ticketResumenService;

    @InjectMocks
    private TicketBffController controller;

    @Test
    void shouldDelegateToServiceWhenResumenIsRequested() {
        UUID idTicket = UUID.randomUUID();
        TicketResumenDTO resumen = new TicketResumenDTO();
        resumen.setIdTicket(idTicket);
        resumen.setTitulo("Pantalla rota");

        when(ticketResumenService.obtenerResumenCompleto(idTicket)).thenReturn(resumen);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket).getBody();

        assertNotNull(result);
        assertEquals(idTicket, result.getIdTicket());
        assertEquals("Pantalla rota", result.getTitulo());
        verify(ticketResumenService).obtenerResumenCompleto(idTicket);
    }

    @Test
    void shouldReturnServiceResponseAsOk() {
        UUID idTicket = UUID.randomUUID();

        TicketResumenDTO resumen = new TicketResumenDTO();
        resumen.setIdTicket(idTicket);
        resumen.setNombreCliente("Cliente Default");

        when(ticketResumenService.obtenerResumenCompleto(idTicket)).thenReturn(resumen);

        TicketResumenDTO result = controller.obtenerResumenCompleto(idTicket).getBody();

        assertNotNull(result);
        assertEquals("Cliente Default", result.getNombreCliente());
    }
}