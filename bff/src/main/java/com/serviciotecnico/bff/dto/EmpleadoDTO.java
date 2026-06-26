package com.serviciotecnico.bff.dto;

import java.util.UUID;

public class EmpleadoDTO {
    private UUID id;
    private String username;

    public EmpleadoDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
