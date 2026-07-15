package com.serviciotecnico.empleado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.serviciotecnico.empleado.dto.CrearEmpleadoRequest;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.exception.ValidationException;
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
        Empleado empleado = new Empleado("kevinval", "kevin@correo.com", "ADMIN");
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

    @Test
    void crearShouldReturnEmpleadoResponse_whenDatosValidos() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest("Nuevo Tecnico", "nuevo@correo.com", "TECNICO");

        when(empleadoRepository.findByEmail("nuevo@correo.com")).thenReturn(Optional.empty());

        EmpleadoResponse response = empleadoService.crear(request);

        assertNotNull(response);
        assertEquals("Nuevo Tecnico", response.getUsername());
        assertEquals("nuevo@correo.com", response.getEmail());
        assertEquals("TECNICO", response.getRol());
        verify(empleadoRepository).save(org.mockito.ArgumentMatchers.any(Empleado.class));
    }

    @Test
    void crearShouldThrow_whenEmailEsNulo() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest("Nombre", null, "TECNICO");

        ValidationException exception = assertThrows(ValidationException.class, () -> empleadoService.crear(request));

        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void crearShouldThrow_whenEmailEsBlanco() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest("Nombre", "   ", "TECNICO");

        ValidationException exception = assertThrows(ValidationException.class, () -> empleadoService.crear(request));

        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void crearShouldThrow_whenUsernameEsNulo() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest(null, "correo@test.com", "TECNICO");

        ValidationException exception = assertThrows(ValidationException.class, () -> empleadoService.crear(request));

        assertEquals("El username es obligatorio", exception.getMessage());
    }

    @Test
    void crearShouldThrow_whenRolEsNulo() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest("Nombre", "correo@test.com", null);

        ValidationException exception = assertThrows(ValidationException.class, () -> empleadoService.crear(request));

        assertEquals("El rol es obligatorio", exception.getMessage());
    }

    @Test
    void crearShouldThrow_whenEmailYaExiste() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest("Nombre", "existente@correo.com", "TECNICO");
        Empleado existente = new Empleado("Otro", "existente@correo.com", "TECNICO");

        when(empleadoRepository.findByEmail("existente@correo.com")).thenReturn(Optional.of(existente));

        ValidationException exception = assertThrows(ValidationException.class, () -> empleadoService.crear(request));

        assertEquals("Ya existe un empleado con ese email", exception.getMessage());
    }
}