package com.serviciotecnico.bff.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.bff.client.AuthClient;
import com.serviciotecnico.bff.dto.AuthResponse;
import com.serviciotecnico.bff.dto.LoginRequest;
import com.serviciotecnico.bff.dto.RegisterRequest;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldDelegateToClient() {
        LoginRequest request = new LoginRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-login");

        when(authClient.login(request)).thenReturn(response);

        AuthResponse result = authService.login(request);

        assertEquals("token-login", result.token());
        verify(authClient).login(request);
    }

    @Test
    void registerShouldDelegateToClient() {
        RegisterRequest request = new RegisterRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-register");

        when(authClient.register(request)).thenReturn(response);

        AuthResponse result = authService.register(request);

        assertEquals("token-register", result.token());
        verify(authClient).register(request);
    }
}