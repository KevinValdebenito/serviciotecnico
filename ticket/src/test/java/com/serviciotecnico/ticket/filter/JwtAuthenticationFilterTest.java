package com.serviciotecnico.ticket.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.serviciotecnico.ticket.util.JwtUtil;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotAuthenticateWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-invalido");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.validateToken("token-invalido")).thenReturn(false);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(jwtUtil).validateToken("token-invalido");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-valido");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.validateToken("token-valido")).thenReturn(true);
        when(jwtUtil.getUsername("token-valido")).thenReturn("tecnico@correo.com");
        when(jwtUtil.getRol("token-valido")).thenReturn("ADMIN");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("tecnico@correo.com", authentication.getPrincipal());
        assertEquals(1, authentication.getAuthorities().size());
        verify(jwtUtil).validateToken("token-valido");
        verify(jwtUtil).getUsername("token-valido");
        verify(jwtUtil).getRol("token-valido");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWithoutAuthoritiesWhenRolIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-sin-rol");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.validateToken("token-sin-rol")).thenReturn(true);
        when(jwtUtil.getUsername("token-sin-rol")).thenReturn("tecnico@correo.com");
        when(jwtUtil.getRol("token-sin-rol")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("tecnico@correo.com", authentication.getPrincipal());
        assertEquals(0, authentication.getAuthorities().size());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueFilterChainWhenJwtUtilThrows() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-que-falla");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.validateToken("token-que-falla")).thenThrow(new RuntimeException("JWT error"));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(jwtUtil).validateToken("token-que-falla");
        verify(filterChain).doFilter(request, response);
    }
}