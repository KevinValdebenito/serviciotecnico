package com.serviciotecnico.empleado.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class EmpleadoDtoTest {

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest("kevin@correo.com", "password123");
        
        assertEquals("kevin@correo.com", request.email());
        assertEquals("password123", request.password());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = new RegisterRequest("kevinval", "password123", "kevin@correo.com", "ADMIN");
        
        assertEquals("kevinval", request.username());
        assertEquals("password123", request.password());
        assertEquals("kevin@correo.com", request.email());
        assertEquals("ADMIN", request.rol());
    }

    @Test
    void testAuthResponse() {
        AuthResponse response = new AuthResponse("token-secreto-123", 3600L, "kevin@correo.com");
        
        assertEquals("token-secreto-123", response.token());
        assertEquals(3600L, response.expiresIn());
        assertEquals("kevin@correo.com", response.email());
    }

    @Test
    void testTicketDto() {
        UUID id = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();
        
        TicketDto dto = new TicketDto(id, "Problema de red", "Sin conexión", "Abierto", "Alta", empleadoId);

        assertEquals(id, dto.id());
        assertEquals("Problema de red", dto.title());
        assertEquals("Sin conexión", dto.description());
        assertEquals("Abierto", dto.status());
        assertEquals("Alta", dto.priority());
        assertEquals(empleadoId, dto.employeeId());
    }

    @Test
    void testEmpleadoResponse() {
        UUID id = UUID.randomUUID();
        // Usamos el constructor generado por @AllArgsConstructor
        EmpleadoResponse response = new EmpleadoResponse(id, "kevinval", "kevin@correo.com", "ADMIN");
        
        // Usamos los getters generados por @Data
        assertEquals(id, response.getId());
        assertEquals("kevinval", response.getUsername());
        assertEquals("kevin@correo.com", response.getEmail());
        assertEquals("ADMIN", response.getRol());

        // Probamos un setter para asegurar cobertura de Lombok
        response.setRol("USER");
        assertEquals("USER", response.getRol());
    }

    @Test
    void testErrorDetails() {
        ErrorDetails error = new ErrorDetails("404", "No encontrado");
        
        assertEquals("404", error.code());
        assertEquals("No encontrado", error.message());
    }

    @Test
    void testApiResponse() {
        // Probamos el constructor que recibe data
        ApiResponse<String> responseConData = new ApiResponse<>(true, "Operación exitosa");
        assertTrue(responseConData.success());
        assertEquals("Operación exitosa", responseConData.data());
        assertNull(responseConData.error());

        // Probamos el constructor que recibe el ErrorDetails
        ErrorDetails error = new ErrorDetails("500", "Error interno");
        ApiResponse<String> responseConError = new ApiResponse<>(false, error);
        assertEquals(false, responseConError.success());
        assertNull(responseConError.data());
        assertEquals("500", responseConError.error().code());
    }
}