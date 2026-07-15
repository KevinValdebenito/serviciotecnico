package com.serviciotecnico.empleado.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.serviciotecnico.empleado.client.TicketClient;
import com.serviciotecnico.empleado.dto.CrearEmpleadoRequest;
import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.dto.TicketDto;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.exception.ValidationException;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final TicketClient ticketClient;

    public EmpleadoService(EmpleadoRepository empleadoRepository, TicketClient ticketClient) {
        this.empleadoRepository = empleadoRepository;
        this.ticketClient = ticketClient;
    }

    public EmpleadoResponse obtenerPorId(UUID id) {
        Empleado emp = empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return new EmpleadoResponse(emp.getId(), emp.getUsername(), emp.getEmail(), emp.getRol());
    }

    public EmpleadoResponse crear(CrearEmpleadoRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new ValidationException("El email es obligatorio");
        }
        if (request.username() == null || request.username().isBlank()) {
            throw new ValidationException("El username es obligatorio");
        }
        if (request.rol() == null || request.rol().isBlank()) {
            throw new ValidationException("El rol es obligatorio");
        }
        if (empleadoRepository.findByEmail(request.email()).isPresent()) {
            throw new ValidationException("Ya existe un empleado con ese email");
        }

        Empleado empleado = new Empleado(request.username(), request.email(), request.rol());
        empleadoRepository.save(empleado);

        return new EmpleadoResponse(empleado.getId(), empleado.getUsername(), empleado.getEmail(), empleado.getRol());
    }

    public List<TicketDto> getTickets(String name, String status) {
        boolean hasFilters = (name != null && !name.isBlank()) || (status != null && !status.isBlank());

        if (hasFilters) {
            return ticketClient.searchTickets(name, status);
        }

        return ticketClient.getAllTickets();
    }
}