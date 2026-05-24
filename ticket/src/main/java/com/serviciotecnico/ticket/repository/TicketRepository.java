package com.serviciotecnico.ticket.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciotecnico.ticket.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}