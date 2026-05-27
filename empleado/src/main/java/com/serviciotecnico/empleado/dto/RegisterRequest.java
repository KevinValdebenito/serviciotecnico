package com.serviciotecnico.empleado.dto;

public record RegisterRequest(
    String username,
    String password,
    String email,
    String rol
) {

}
