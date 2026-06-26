package com.serviciotecnico.ticket.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.serviciotecnico.ticket.model.Ticket;

@SpringBootTest
@ActiveProfiles("test")
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldSaveAndFindTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Falla de disco duro");
        ticket.setDescription("El disco hace ruido");
        ticket.setStatus("Abierto");
        ticket.setPriority("Alta");
        ticket.setClientEmail("cliente@correo.com");

        Ticket savedTicket = ticketRepository.save(ticket);
        Optional<Ticket> result = ticketRepository.findById(savedTicket.getId());

        assertNotNull(savedTicket.getId());
        assertTrue(result.isPresent());
    }
}
