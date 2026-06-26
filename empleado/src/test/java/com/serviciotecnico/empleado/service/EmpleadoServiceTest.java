package com.serviciotecnico.empleado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

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
}