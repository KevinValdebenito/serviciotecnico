package com.serviciotecnico.auth.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testUserEntity() {
        User user = new User();
        user.setEmail("test@correo.com");
        user.setPassword("pass123");
        user.setActive(false);

        assertEquals("test@correo.com", user.getEmail());
        assertEquals("pass123", user.getPassword());
        assertFalse(user.isActive());

        User user2 = new User("nuevo@correo.com", "pass456");
        assertEquals("nuevo@correo.com", user2.getEmail());
        assertEquals("pass456", user2.getPassword());
        assertTrue(user2.isActive());
    }

}
