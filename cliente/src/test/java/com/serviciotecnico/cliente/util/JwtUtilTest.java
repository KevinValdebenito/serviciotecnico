package com.serviciotecnico.cliente.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private static final String SECRET = "clave-secreta-para-test-de-jwt-util-cliente";

    private JwtUtil jwtUtil;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);

        // Replicamos la misma derivación de clave que usa JwtUtil internamente (SHA-256 sobre el secreto)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET.getBytes(StandardCharsets.UTF_8));
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(String email, String rol, long expirationMillisFromNow) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillisFromNow);

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
    void validateToken_deberiaSerTrue_paraTokenValido() {
        String token = buildToken("cliente@test.com", "CLIENTE", 60_000);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_deberiaSerFalse_paraTokenMalformado() {
        assertFalse(jwtUtil.validateToken("esto-no-es-un-token-valido"));
    }

    @Test
    void validateToken_deberiaSerFalse_paraTokenExpirado() {
        String tokenExpirado = buildToken("cliente@test.com", "CLIENTE", -60_000);

        assertFalse(jwtUtil.validateToken(tokenExpirado));
    }

    @Test
    void getUsername_deberiaRetornarElEmail_paraTokenValido() {
        String token = buildToken("cliente@test.com", "CLIENTE", 60_000);

        assertEquals("cliente@test.com", jwtUtil.getUsername(token));
    }

    @Test
    void getUsername_deberiaRetornarNull_paraTokenInvalido() {
        assertNull(jwtUtil.getUsername("token-invalido"));
    }

    @Test
    void getRol_deberiaRetornarElRol_paraTokenValido() {
        String token = buildToken("admin@test.com", "ADMIN", 60_000);

        assertEquals("ADMIN", jwtUtil.getRol(token));
    }

    @Test
    void getRol_deberiaRetornarNull_siElTokenNoTraeRol() {
        String token = buildToken("cliente@test.com", null, 60_000);

        assertNull(jwtUtil.getRol(token));
    }

    @Test
    void getRol_deberiaRetornarNull_paraTokenInvalido() {
        assertNull(jwtUtil.getRol("token-invalido"));
    }
}