package com.serviciotecnico.ticket.util;

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

    private JwtUtil jwtUtil;
    private final String SECRET_TEST = "mi-secreto-super-seguro-para-hacer-testing-local";
    private SecretKey testKey;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        // Inyectamos el valor del secreto simulando el @Value de Spring
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET_TEST);

        // Replicamos la generación de la llave para firmar nuestros tokens de prueba
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET_TEST.getBytes(StandardCharsets.UTF_8));
        testKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // --- MÉTODOS DE AYUDA PARA EL TEST ---

    private String generarTokenValido(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validez
                .signWith(testKey)
                .compact();
    }

    private String generarTokenSinRol(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(testKey)
                .compact();
    }

    // --- TESTS DE VALIDACIÓN ---

    @Test
    void validateToken_conTokenValido_retornaTrue() {
        String token = generarTokenValido("k.valdebenito", "ADMIN");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_conTokenInvalido_retornaFalse() {
        assertFalse(jwtUtil.validateToken("este.token.es.falso"));
    }

    // --- TESTS DE EXTRACCIÓN DE USUARIO ---

    @Test
    void getUsername_conTokenValido_retornaUsername() {
        String token = generarTokenValido("k.valdebenito", "ADMIN");
        assertEquals("k.valdebenito", jwtUtil.getUsername(token));
    }

    @Test
    void getUsername_conTokenInvalido_retornaNull() {
        assertNull(jwtUtil.getUsername("este.token.es.falso"));
    }

    // --- TESTS DE EXTRACCIÓN DE ROL ---

    @Test
    void getRol_conTokenValido_retornaRol() {
        String token = generarTokenValido("k.valdebenito", "TECNICO");
        assertEquals("TECNICO", jwtUtil.getRol(token));
    }

    @Test
    void getRol_conTokenSinRol_retornaNull() {
        String token = generarTokenSinRol("k.valdebenito");
        assertNull(jwtUtil.getRol(token));
    }

    @Test
    void getRol_conTokenInvalido_retornaNull() {
        assertNull(jwtUtil.getRol("este.token.es.falso"));
    }
}