package com.serviciotecnico.auth.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secret = "mi-clave-secreta-super-segura-para-el-test-de-jwt";
        long expirationHours = 1L;

        jwtService = new JwtService(secret, expirationHours);
    }

    @Test
    void testGenerateToken(){
        String email = "kevin@correo.com";
        String token = jwtService.generateToken(email);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    void testSha256Exception() {
        
    }
}
