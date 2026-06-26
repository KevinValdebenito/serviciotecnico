package com.serviciotecnico.bff.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.serviciotecnico.bff.dto.AuthResponse;
import com.serviciotecnico.bff.dto.LoginRequest;
import com.serviciotecnico.bff.dto.RegisterRequest;

class AuthClientTest {

    private RestClient restClient;
    private AuthClient authClient;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        authClient = new AuthClient(restClient, "http://auth-service");
    }

    @Test
    void loginShouldReturnAuthResponse() {
        LoginRequest request = new LoginRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-login");

        when(restClient.post().uri("http://auth-service/login").body(request).retrieve().body(AuthResponse.class))
            .thenReturn(response);

        AuthResponse result = authClient.login(request);

        assertEquals("token-login", result.token());
    }

    @Test
    void registerShouldReturnAuthResponse() {
        RegisterRequest request = new RegisterRequest("user@mail.com", "secret");
        AuthResponse response = new AuthResponse("token-register");

        when(restClient.post().uri("http://auth-service/register").body(request).retrieve().body(AuthResponse.class))
            .thenReturn(response);

        AuthResponse result = authClient.register(request);

        assertEquals("token-register", result.token());
    }
}