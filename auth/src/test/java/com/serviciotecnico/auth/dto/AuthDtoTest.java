package com.serviciotecnico.auth.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthDtoTest {

    @Test
    void testAuthResponse() {
        AuthResponse response = new AuthResponse("token-magico-123");
        assertEquals("token-magico-123", response.token());
    }

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest("kevin@correo.com", "password123");
        assertEquals("kevin@correo.com", request.email());
        assertEquals("password123", request.password());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = new RegisterRequest("kevin@correo.com", "password123");
        assertEquals("kevin@correo.com", request.email());
        assertEquals("password123", request.password());
    }

}
