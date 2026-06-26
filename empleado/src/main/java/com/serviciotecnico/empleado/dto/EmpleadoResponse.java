package com.serviciotecnico.empleado.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpleadoResponse {
    private UUID id;
    private String username;
    private String email;
    private String rol;
}
