package com.serviciotecnico.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.auth.domain.User;
import com.serviciotecnico.auth.dto.AuthResponse;
import com.serviciotecnico.auth.dto.LoginRequest;
import com.serviciotecnico.auth.dto.RegisterRequest;
import com.serviciotecnico.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("kevin@correo.com", "encoded-password");
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("kevin@correo.com", "password123");

        when(userRepository.findByEmailAndActiveTrue("kevin@correo.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken("kevin@correo.com")).thenReturn("mock-token");

        AuthResponse response = authService.login(request);

        assertEquals("mock-token", response.token());
        verify(userRepository).findByEmailAndActiveTrue("kevin@correo.com");
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequest request = new LoginRequest("kevin@correo.com", "wrong-password");

        when(userRepository.findByEmailAndActiveTrue("kevin@correo.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("nuevo@correo.com", "password123");

        when(userRepository.existsById("nuevo@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(jwtService.generateToken("nuevo@correo.com")).thenReturn("mock-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock-token", response.token());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterExistingUser() {
        RegisterRequest request = new RegisterRequest("kevin@correo.com", "password123");

        when(userRepository.existsById("kevin@correo.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> authService.register(request));
    }
}
