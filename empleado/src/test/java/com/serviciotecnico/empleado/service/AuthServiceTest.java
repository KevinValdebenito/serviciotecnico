package com.serviciotecnico.empleado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.serviciotecnico.empleado.dto.AuthResponse;
import com.serviciotecnico.empleado.dto.LoginRequest;
import com.serviciotecnico.empleado.dto.RegisterRequest;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.exception.AuthenticationException;
import com.serviciotecnico.empleado.exception.ValidationException;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;
import com.serviciotecnico.empleado.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("kevinval", "password123", "kevin@correo.com", "ADMIN");
        loginRequest = new LoginRequest("kevin@correo.com", "password123");
    }

    @Test
    void registerShouldCreateEmpleadoAndReturnAuthResponse() {
        when(empleadoRepository.findByEmail("kevin@correo.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hash-password");
        when(jwtUtil.generateToken("kevin@correo.com")).thenReturn("token-generado");
        when(jwtUtil.getExpiration()).thenReturn(3600L);

        AuthResponse response = authService.register(registerRequest);

        assertEquals("token-generado", response.token());
        assertEquals(3600L, response.expiresIn());
        assertEquals("kevin@correo.com", response.email());

        verify(empleadoRepository).save(any(Empleado.class));
        verify(passwordEncoder).encode("password123");
        verify(jwtUtil).generateToken("kevin@correo.com");
    }

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {
        when(empleadoRepository.findByEmail("kevin@correo.com")).thenReturn(Optional.of(new Empleado()));

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.register(registerRequest));

        assertEquals("Email ya registrado", exception.getMessage());
    }

    @Test
    void registerShouldFailWhenPasswordIsBlank() {
        RegisterRequest invalidRequest = new RegisterRequest("kevinval", "", "kevin2@correo.com", "ADMIN");
        when(empleadoRepository.findByEmail("kevin2@correo.com")).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.register(invalidRequest));

        assertEquals("La contraseña es obligatoria", exception.getMessage());
    }

    @Test
    void registerShouldFailWhenEmailIsBlank() {
        RegisterRequest invalidRequest = new RegisterRequest("kevinval", "password123", "", "ADMIN");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.register(invalidRequest));

        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void loginShouldReturnAuthResponseWhenCredentialsAreValid() {
        Empleado empleado = new Empleado("kevinval", "kevin@correo.com", "hash-password", "ADMIN");

        when(empleadoRepository.findByEmail("kevin@correo.com")).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches("password123", "hash-password")).thenReturn(true);
        when(jwtUtil.generateToken("kevin@correo.com")).thenReturn("token-generado");
        when(jwtUtil.getExpiration()).thenReturn(3600L);

        AuthResponse response = authService.login(loginRequest);

        assertEquals("token-generado", response.token());
        assertEquals(3600L, response.expiresIn());
        assertEquals("kevin@correo.com", response.email());
    }

    @Test
    void loginShouldFailWhenPasswordDoesNotMatch() {
        Empleado empleado = new Empleado("kevinval", "kevin@correo.com", "hash-password", "ADMIN");

        when(empleadoRepository.findByEmail("kevin@correo.com")).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches("password123", "hash-password")).thenReturn(false);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.login(loginRequest));

        assertEquals("Credenciales incorrectas", exception.getMessage());
    }

    @Test
    void loginShouldFailWhenEmailIsBlank() {
        LoginRequest invalidRequest = new LoginRequest("", "password123");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.login(invalidRequest));

        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void loginShouldFailWhenPasswordIsBlank() {
        LoginRequest invalidRequest = new LoginRequest("kevin@correo.com", "");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.login(invalidRequest));

        assertEquals("La contraseña es obligatoria", exception.getMessage());
    }

    @Test
    void validateTokenShouldDelegateToJwtUtil() {
        when(jwtUtil.validateToken("token-valido")).thenReturn(true);

        boolean valid = authService.validateToken("token-valido");

        assertTrue(valid);
        verify(jwtUtil).validateToken("token-valido");
    }

    @Test
    void validateTokenShouldReturnFalseWhenJwtUtilRejectsToken() {
        when(jwtUtil.validateToken("token-invalido")).thenReturn(false);

        boolean valid = authService.validateToken("token-invalido");

        assertFalse(valid);
        verify(jwtUtil).validateToken("token-invalido");
    }
}