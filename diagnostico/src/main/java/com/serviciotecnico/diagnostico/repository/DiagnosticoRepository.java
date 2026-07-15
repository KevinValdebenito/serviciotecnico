package com.serviciotecnico.diagnostico.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.diagnostico.entity.Diagnostico;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, UUID> {
    List<Diagnostico> findByTicketId(UUID ticketId);
}