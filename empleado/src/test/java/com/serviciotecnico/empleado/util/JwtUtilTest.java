package com.serviciotecnico.empleado.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        
        // Inyectamos valores manualmente usando Reflection para saltar el @Value
        setPrivateField(jwtUtil, "secret", "una-clave-secreta-muy-larga-y-segura-de-32-caracteres");
        setPrivateField(jwtUtil, "expiration", 3600000L); // 1 hora
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("test@correo.com");
        
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("test@correo.com", jwtUtil.getUsername(token));
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertFalse(jwtUtil.validateToken("token-invalido"));
    }

    @Test
    void shouldReturnNullForUsernameInInvalidToken() {
        assertNotNull(jwtUtil.getExpiration());
        assertNull(jwtUtil.getUsername("invalido"));
    }
}