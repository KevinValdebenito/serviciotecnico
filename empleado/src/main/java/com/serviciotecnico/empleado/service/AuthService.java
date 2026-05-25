package com.serviciotecnico.empleado.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.serviciotecnico.empleado.dto.AuthResponse;
import com.serviciotecnico.empleado.dto.LoginRequest;
import com.serviciotecnico.empleado.dto.RegisterRequest;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;
import com.serviciotecnico.empleado.util.JwtUtil;
import com.serviciotecnico.empleado.exception.AuthenticationException;
import com.serviciotecnico.empleado.exception.ValidationException;



@Service
public class AuthService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request){
        if (empleadoRepository.findByEmail(request.email()).isPresent()) {
        throw new ValidationException("Email ya registrado");
        }

        if (request.email() == null || request.email().isBlank()) {
        throw new ValidationException("El email es obligatorio");
        }

        if (request.password() == null || request.password().isBlank()) {
        throw new ValidationException("La contraseña es obligatoria");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        Empleado empleado = Empleado.builder()
        .username(request.username())
        .email(request.email())
        .passwordHash(passwordHash)
        .rol(request.rol())
        .build();

        empleadoRepository.save(empleado);

        String token = jwtUtil.generateToken(empleado.getEmail());

        return new AuthResponse(token, jwtUtil.getExpiration(), empleado.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        if (request.email() == null || request. email().isBlank()){
            throw new ValidationException("El email es obligatorio");
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new ValidationException("La contraseña es obligatoria");
        }

        Empleado empleado = empleadoRepository.findByEmail(request.email()).orElseThrow(() -> new AuthenticationException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.password(), empleado.getPasswordHash())) {
            throw new AuthenticationException("Credenciales incorrectas");
        }

        String token =jwtUtil.generateToken(empleado.getEmail());

        return new AuthResponse(token, jwtUtil.getExpiration(), empleado.getEmail());
    }

    public boolean validateToken(String token){
        return jwtUtil.validateToken(token);
    }

}
