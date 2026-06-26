package com.serviciotecnico.cliente.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.serviciotecnico.cliente.entity.Cliente;

@SpringBootTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void shouldSaveAndFindCliente() {
        // 1. Arrange: Creamos un cliente de prueba
        Cliente cliente = new Cliente("prueba@correo.com", "11.222.333-4", "Cliente de Prueba", "987654321", "Providencia");

        // 2. Act: Lo guardamos en la base de datos en memoria (H2)
        Cliente savedCliente = clienteRepository.save(cliente);
        Optional<Cliente> result = clienteRepository.findById(savedCliente.getEmail());

        // 3. Assert: Verificamos que se haya guardado usando el email como ID
        assertNotNull(savedCliente.getEmail());
        assertTrue(result.isPresent());
        assertEquals("Cliente de Prueba", result.get().getNombreCompleto());
    }
}