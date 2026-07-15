package com.serviciotecnico.empleado.dto;

public record CrearEmpleadoRequest(
    String username,
    String email,
    String rol
) {

}
