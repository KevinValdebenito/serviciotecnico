package com.serviciotecnico.empleado.dto;

public record LoginRequest(
    String email,
    String password
) {
}  
