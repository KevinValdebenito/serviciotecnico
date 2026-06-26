package com.serviciotecnico.empleado.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.serviciotecnico.empleado.dto.EmpleadoResponse;
import com.serviciotecnico.empleado.entity.Empleado;
import com.serviciotecnico.empleado.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public EmpleadoResponse obtenerPorId(UUID id) {
        Empleado emp = empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return new EmpleadoResponse(emp.getId(), emp.getUsername(), emp.getEmail(), emp.getRol());
    }
}
