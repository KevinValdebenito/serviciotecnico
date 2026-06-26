package com.serviciotecnico.empleado.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.empleado.entity.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Optional<Empleado> findByEmail(String email);
}
