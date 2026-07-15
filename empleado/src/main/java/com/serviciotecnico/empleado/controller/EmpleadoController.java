package com.serviciotecnico.empleado.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.empleado.dto.ApiResponse;
import com.serviciotecnico.empleado.dto.CrearEmpleadoRequest;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpleadoResponse>> crearEmpleado(@RequestBody CrearEmpleadoRequest request) {
        EmpleadoResponse creado = empleadoService.crear(request);
        return new ResponseEntity<>(new ApiResponse<>(true, creado), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> getEmpleadoById(@PathVariable UUID id){
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
    }

    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<List<TicketDto>>> getAllTickets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        List<TicketDto> tickets = empleadoService.getTickets(name, status);

        ApiResponse<List<TicketDto>> response = new ApiResponse<>(true, tickets);
        return ResponseEntity.ok(response);
    }
}