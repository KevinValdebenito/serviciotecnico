package com.serviciotecnico.empleado.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private static final String SECRET = "una-clave-secreta-muy-larga-y-segura-de-32-caracteres";

    private JwtUtil jwtUtil;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // Inyectamos valores manualmente usando Reflection para saltar el @Value
        setPrivateField(jwtUtil, "secret", SECRET);
        setPrivateField(jwtUtil, "expiration", 3600000L); // 1 hora

        // Replicamos la misma derivación de clave (SHA-256) para construir tokens manualmente
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET.getBytes(StandardCharsets.UTF_8));
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private String buildTokenConRol(String email, String rol, long msDesdeAhora) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + msDesdeAhora);

        var builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry);

        if (rol != null) {
            builder.claim("rol", rol);
        }

        return builder.signWith(signingKey).compact();
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

    @Test
    void shouldReturnFalseForExpiredToken() {
        String tokenExpirado = buildTokenConRol("test@correo.com", "ADMIN", -60_000);

        assertFalse(jwtUtil.validateToken(tokenExpirado));
    }

    @Test
    void shouldReturnRol_whenTokenHasRolClaim() {
        String token = buildTokenConRol("admin@correo.com", "ADMIN", 60_000);

        assertEquals("ADMIN", jwtUtil.getRol(token));
    }

    @Test
    void shouldReturnNullRol_whenTokenHasNoRolClaim() {
        String token = buildTokenConRol("sinrol@correo.com", null, 60_000);

        assertNull(jwtUtil.getRol(token));
    }

    @Test
    void shouldReturnNullRol_forInvalidToken() {
        assertNull(jwtUtil.getRol("token-invalido"));
    }
}