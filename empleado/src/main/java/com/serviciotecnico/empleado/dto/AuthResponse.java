package com.serviciotecnico.empleado.dto;

public record AuthResponse(
    String token,
    long expiresIn,
    String email
) {
}
