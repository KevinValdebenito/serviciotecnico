package com.serviciotecnico.equipo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.equipo.entity.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, UUID> {
    List<Equipo> findByClienteEmail(String clienteEmail);
}