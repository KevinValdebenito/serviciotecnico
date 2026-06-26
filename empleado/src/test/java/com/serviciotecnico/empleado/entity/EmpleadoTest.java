package com.serviciotecnico.empleado.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void shouldCreateEmpleadoWithNoArgsConstructorAndSetters() {

        Empleado empleado = new Empleado();
        UUID id = UUID.randomUUID();

        empleado.setId(id);
        empleado.setUsername("kevinval");
        empleado.setEmail("kevin@correo.com");
        empleado.setPasswordHash("hashedpassword123");
        empleado.setRol("ADMIN");

        assertEquals(id, empleado.getId());
        assertEquals("kevinval", empleado.getUsername());
        assertEquals("kevin@correo.com", empleado.getEmail());
        assertEquals("hashedpassword123", empleado.getPasswordHash());
        assertEquals("ADMIN", empleado.getRol());
    }

    @Test
    void shouldCreateEmpleadoWithAllArgsConstructor() {

        Empleado empleado = new Empleado("admin", "admin@correo.com", "pass123", "SUPERADMIN");

        assertEquals("admin", empleado.getUsername());
        assertEquals("admin@correo.com", empleado.getEmail());
        assertEquals("pass123", empleado.getPasswordHash());
        assertEquals("SUPERADMIN", empleado.getRol());
        assertNull(empleado.getId()); 
    }

    @Test
    void shouldSetTimestampsOnCreateAndUpdate() {

        Empleado empleado = new Empleado();
        assertNull(empleado.getCreatedAt());
        assertNull(empleado.getUpdatedAt());

        empleado.onCreate();

        assertNotNull(empleado.getCreatedAt());
        assertNotNull(empleado.getUpdatedAt());

        empleado.onUpdate();

        assertNotNull(empleado.getUpdatedAt());
    }

    @Test
    void shouldTestRemainingSetters() {

        Empleado empleado = new Empleado();
        LocalDateTime ahora = LocalDateTime.now();

        empleado.setCreatedAt(ahora);
        empleado.setUpdatedAt(ahora);

        assertEquals(ahora, empleado.getCreatedAt());
        assertEquals(ahora, empleado.getUpdatedAt());
    }
}