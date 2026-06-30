package com.serviciotecnico.cliente.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.cliente.dto.ClienteDto;
import com.serviciotecnico.cliente.entity.Cliente;
import com.serviciotecnico.cliente.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void getAllClientes_shouldReturnDtoList() {
        // Arrange
        Cliente cliente = new Cliente("kevin@correo.com", "12.345.678-9", "Kevin Valdebenito", "912345678", "Santiago");
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // Act
        List<ClienteDto> result = clienteService.getAllClientes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Kevin Valdebenito", result.get(0).nombreCompleto());
    }

    @Test
    void getClienteByEmail_shouldReturnClienteDto_whenExists() {
        // Arrange
        String email = "kevin@correo.com";
        Cliente cliente = new Cliente(email, "12.345.678-9", "Kevin Valdebenito", "912345678", "Santiago");
        when(clienteRepository.findById(email)).thenReturn(Optional.of(cliente));

        // Act
        ClienteDto result = clienteService.getClienteByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.email());
    }

    @Test
    void getClienteByEmail_shouldThrowException_whenNotExists() {
        // Arrange
        String email = "noexiste@correo.com";
        when(clienteRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clienteService.getClienteByEmail(email));
        assertEquals("Cliente no encontrado con email: " + email, exception.getReason());
    }

    @Test
    void createCliente_shouldSaveAndReturnDto() {
        // Arrange
        ClienteDto inputDto = new ClienteDto("kevin@correo.com", "12.345.678-9", "Kevin Valdebenito", "912345678", "Santiago");
        Cliente clienteGuardado = new Cliente("kevin@correo.com", "12.345.678-9", "Kevin Valdebenito", "912345678", "Santiago");
        
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act
        ClienteDto result = clienteService.createCliente(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("Kevin Valdebenito", result.nombreCompleto());
    }

    @Test
    void updateCliente_shouldUpdateAndReturnDto_whenExists() {
        // Arrange
        String email = "kevin@correo.com";
        Cliente clienteExistente = new Cliente(email, "12.345.678-9", "Kevin Antiguo", "912345678", "Santiago");
        ClienteDto inputDto = new ClienteDto(email, "12.345.678-9", "Kevin Actualizado", "987654321", "El Bosque");
        Cliente clienteActualizado = new Cliente(email, "12.345.678-9", "Kevin Actualizado", "987654321", "El Bosque");

        when(clienteRepository.findById(email)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        // Act
        ClienteDto result = clienteService.updateCliente(email, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("Kevin Actualizado", result.nombreCompleto());
        assertEquals("El Bosque", result.direccion());
    }

    @Test
    void deleteCliente_shouldDelete_whenExists() {
        // Arrange
        String email = "kevin@correo.com";
        Cliente cliente = new Cliente(email, "12.345.678-9", "Kevin Valdebenito", "912345678", "Santiago");
        when(clienteRepository.findById(email)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.deleteCliente(email);

        // Assert
        verify(clienteRepository).delete(cliente);
    }

    @Test
    void updateCliente_shouldThrowWhenNotExists() {
        String email = "noexiste@correo.com";
        ClienteDto inputDto = new ClienteDto(email, "12.345.678-9", "Kevin", "912345678", "Santiago");

        when(clienteRepository.findById(email)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clienteService.updateCliente(email, inputDto));

        assertEquals("Cliente no encontrado con email: " + email, exception.getReason());
    }

    @Test
    void deleteCliente_shouldThrowWhenNotExists() {
        String email = "noexiste@correo.com";
        when(clienteRepository.findById(email)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clienteService.deleteCliente(email));

        assertEquals("Cliente no encontrado con email: " + email, exception.getReason());
    }
}