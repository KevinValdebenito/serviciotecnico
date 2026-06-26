package com.serviciotecnico.bff.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtTokenServiceTest {

    private static final String SECRET = "mi-sercreto-bff-de-prueba-1234567890";

    @Test
    void extractSubjectShouldReturnSubjectWhenTokenIsValid() {
        JwtTokenService jwtTokenService = new JwtTokenService(SECRET);
        SecretKey signingKey = signingKeyFrom(SECRET);
        String token = Jwts.builder()
            .subject("usuario@mail.com")
            .signWith(signingKey)
            .compact();

        String subject = jwtTokenService.extractSubject(token);

        assertEquals("usuario@mail.com", subject);
    }

    @Test
    void extractSubjectShouldThrowWhenTokenIsInvalid() {
        JwtTokenService jwtTokenService = new JwtTokenService(SECRET);

        assertThrows(JwtException.class, () -> jwtTokenService.extractSubject("token-invalido"));
    }

    private static SecretKey signingKeyFrom(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}