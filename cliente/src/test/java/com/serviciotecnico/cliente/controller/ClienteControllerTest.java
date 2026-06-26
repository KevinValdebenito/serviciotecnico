package com.serviciotecnico.cliente.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.serviciotecnico.cliente.dto.ClienteDto;
import com.serviciotecnico.cliente.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Test
    void getAllClientes_shouldReturnOk() {
        // Arrange
        ClienteController controller = new ClienteController(clienteService);
        ClienteDto mockDto = new ClienteDto("kevin@correo.com", "12.345.678-9", "Kevin", "912345678", "Santiago");
        when(clienteService.getAllClientes()).thenReturn(List.of(mockDto));

        // Act
        ResponseEntity<List<ClienteDto>> result = controller.getAllClientes();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getClienteByEmail_shouldReturnOk() {
        // Arrange
        ClienteController controller = new ClienteController(clienteService);
        String email = "kevin@correo.com";
        ClienteDto mockDto = new ClienteDto(email, "12.345.678-9", "Kevin", "912345678", "Santiago");
        when(clienteService.getClienteByEmail(email)).thenReturn(mockDto);

        // Act
        ResponseEntity<ClienteDto> result = controller.getClienteByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(email, result.getBody().email());
    }

    @Test
    void createCliente_shouldReturnCreated() {
        // Arrange
        ClienteController controller = new ClienteController(clienteService);
        ClienteDto inputDto = new ClienteDto("kevin@correo.com", "12.345.678-9", "Kevin", "912345678", "Santiago");
        when(clienteService.createCliente(inputDto)).thenReturn(inputDto);

        // Act
        ResponseEntity<ClienteDto> result = controller.createCliente(inputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void updateCliente_shouldReturnOk() {
        // Arrange
        ClienteController controller = new ClienteController(clienteService);
        String email = "kevin@correo.com";
        ClienteDto inputDto = new ClienteDto(email, "12.345.678-9", "Kevin", "912345678", "Santiago");
        when(clienteService.updateCliente(email, inputDto)).thenReturn(inputDto);

        // Act
        ResponseEntity<ClienteDto> result = controller.updateCliente(email, inputDto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void deleteCliente_shouldReturnNoContent() {
        // Arrange
        ClienteController controller = new ClienteController(clienteService);
        String email = "kevin@correo.com";

        // Act
        ResponseEntity<Void> result = controller.deleteCliente(email);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(clienteService).deleteCliente(email);
    }
}