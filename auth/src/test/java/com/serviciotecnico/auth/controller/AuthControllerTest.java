package com.serviciotecnico.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.serviciotecnico.auth.dto.AuthResponse;
import com.serviciotecnico.auth.dto.LoginRequest;
import com.serviciotecnico.auth.dto.RegisterRequest;
import com.serviciotecnico.auth.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void testLogin() throws Exception {
        AuthResponse mockResponse =  new AuthResponse("token-falso-login");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);
        
        String requestBody =  "{\"email\":\"kevin@correo.com\",\"password\":\"clave123\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-falso-login"));
    }

    @Test
    void testRegister() throws Exception {
        AuthResponse mockResponse = new AuthResponse("token-falso-register");
        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);

        String requestBody = "{\"email\":\"kevin@correo.com\",\"password\":\"clave123\"}";

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-falso-register"));
    }
}
