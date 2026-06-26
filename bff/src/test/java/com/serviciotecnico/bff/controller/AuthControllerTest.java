package com.serviciotecnico.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.bff.dto.AuthResponse;
import com.serviciotecnico.bff.dto.LoginRequest;
import com.serviciotecnico.bff.dto.RegisterRequest;
import com.serviciotecnico.bff.services.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginShouldDelegateToService() {
        LoginRequest request = new LoginRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-123");

        when(authService.login(request)).thenReturn(response);

        AuthResponse result = authController.login(request);

        assertEquals("token-123", result.token());
        verify(authService).login(request);
    }

    @Test
    void registerShouldDelegateToService() {
        RegisterRequest request = new RegisterRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-456");

        when(authService.register(request)).thenReturn(response);

        AuthResponse result = authController.register(request);

        assertEquals("token-456", result.token());
        verify(authService).register(request);
    }
}