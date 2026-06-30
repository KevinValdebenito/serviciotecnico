package com.serviciotecnico.empleado.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.serviciotecnico.empleado.dto.ApiResponse;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.service.EmpleadoService;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    @Test
    void getEmpleadoByIdShouldReturnEmpleadoResponse() {
        UUID id = UUID.randomUUID();
        EmpleadoResponse expected = new EmpleadoResponse(id, "kevinval", "kevin@correo.com", "ADMIN");

        when(empleadoService.obtenerPorId(id)).thenReturn(expected);

        ResponseEntity<EmpleadoResponse> response = empleadoController.getEmpleadoById(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("kevinval", response.getBody().getUsername());
        verify(empleadoService).obtenerPorId(id);
    }

    @Test
    void getAllTicketsShouldUseGetAllTicketsWhenNoFilters() {
        List<TicketDto> tickets = List.of(
            new TicketDto(UUID.randomUUID(), "Problema de red", "Sin conexión", "Abierto", "Alta", UUID.randomUUID())
        );

        when(empleadoService.getTickets(null, null)).thenReturn(tickets);

        ResponseEntity<ApiResponse<List<TicketDto>>> response = empleadoController.getAllTickets(null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().success());
        assertEquals(1, response.getBody().data().size());
        verify(empleadoService).getTickets(null, null);
    }

    @Test
    void getAllTicketsShouldUseSearchTicketsWhenNameFilterIsPresent() {
        List<TicketDto> tickets = List.of(
            new TicketDto(UUID.randomUUID(), "Cambio de pantalla", "Pantalla rota", "Abierto", "Media", UUID.randomUUID())
        );

        when(empleadoService.getTickets("pantalla", null)).thenReturn(tickets);

        ResponseEntity<ApiResponse<List<TicketDto>>> response = empleadoController.getAllTickets("pantalla", null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().success());
        assertEquals(1, response.getBody().data().size());
        verify(empleadoService).getTickets("pantalla", null);
    }

    @Test
    void getAllTicketsShouldUseSearchTicketsWhenStatusFilterIsPresent() {
        List<TicketDto> tickets = List.of(
            new TicketDto(UUID.randomUUID(), "Cambio de teclado", "Tecla dañada", "Cerrado", "Baja", UUID.randomUUID())
        );

        when(empleadoService.getTickets(null, "Cerrado")).thenReturn(tickets);

        ResponseEntity<ApiResponse<List<TicketDto>>> response = empleadoController.getAllTickets(null, "Cerrado");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().success());
        assertEquals(1, response.getBody().data().size());
        verify(empleadoService).getTickets(null, "Cerrado");
    }
}