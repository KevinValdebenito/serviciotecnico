package com.serviciotecnico.repuesto.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.repuesto.entity.Repuesto;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, UUID> {
}