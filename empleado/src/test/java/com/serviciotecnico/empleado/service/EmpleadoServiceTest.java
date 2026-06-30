package com.serviciotecnico.empleado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.serviciotecnico.empleado.client.TicketClient;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void obtenerPorIdShouldReturnEmpleadoResponseWhenEmpleadoExists() {
        UUID id = UUID.randomUUID();
        Empleado empleado = new Empleado("kevinval", "kevin@correo.com", "hash-password", "ADMIN");
        empleado.setId(id);

        when(empleadoRepository.findById(id)).thenReturn(Optional.of(empleado));

        EmpleadoResponse response = empleadoService.obtenerPorId(id);

        assertEquals(id, response.getId());
        assertEquals("kevinval", response.getUsername());
        assertEquals("kevin@correo.com", response.getEmail());
        assertEquals("ADMIN", response.getRol());
    }

    @Test
    void obtenerPorIdShouldThrowWhenEmpleadoDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(empleadoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> empleadoService.obtenerPorId(id));

        assertEquals("Empleado no encontrado", exception.getMessage());
    }

    @Test
    void getTicketsShouldReturnAllWhenNoFilters() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Problema", "Detalle", "Abierto", "Alta", UUID.randomUUID()));
        when(ticketClient.getAllTickets()).thenReturn(tickets);

        List<TicketDto> result = empleadoService.getTickets(null, null);

        assertEquals(1, result.size());
        verify(ticketClient).getAllTickets();
    }

    @Test
    void getTicketsShouldSearchWhenFiltersExist() {
        List<TicketDto> tickets = List.of(new TicketDto(UUID.randomUUID(), "Pantalla", "Detalle", "Abierto", "Alta", UUID.randomUUID()));
        when(ticketClient.searchTickets("pantalla", "Abierto")).thenReturn(tickets);

        List<TicketDto> result = empleadoService.getTickets("pantalla", "Abierto");

        assertEquals(1, result.size());
        verify(ticketClient).searchTickets("pantalla", "Abierto");
    }
}