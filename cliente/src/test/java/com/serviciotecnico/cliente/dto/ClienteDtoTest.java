package com.serviciotecnico.cliente.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ClienteDtoTest {

    @Test
    void shouldCreateRecordDto() {

        ClienteDto dto = new ClienteDto("prueba@correo.com",
                                        "9.876.543-2",
                                        "Cliente Prueba",
                                        "987654321",
                                        "Providencia");

        assertEquals("prueba@correo.com", dto.email());
        assertEquals("9.876.543-2", dto.rut());
        assertEquals("Cliente Prueba", dto.nombreCompleto());
        assertEquals("987654321", dto.telefono());
        assertEquals("Providencia", dto.direccion());
    }
}