package com.serviciotecnico.ticket.service;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.serviciotecnico.ticket.dto.TicketDto;
import com.serviciotecnico.ticket.model.Ticket;
import com.serviciotecnico.ticket.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketDto getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        return convertToDto(ticket);
    }

    @Override
    public List<TicketDto> searchTickets(String title, String status, String priority, UUID employeeId) {
        return ticketRepository.findAll().stream()
                .filter(ticket -> title == null || title.isBlank()
                        || ticket.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(ticket -> status == null || status.isBlank()
                        || ticket.getStatus().equalsIgnoreCase(status))
                .filter(ticket -> priority == null || priority.isBlank()
                        || ticket.getPriority().equalsIgnoreCase(priority))
                .filter(ticket -> employeeId == null
                        || Objects.equals(ticket.getEmployeeId(), employeeId))
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        Ticket ticket = convertToEntity(ticketDto);
        Ticket savedTicket = ticketRepository.save(ticket);

        return convertToDto(savedTicket);
    }

    @Override
    public TicketDto updateTicket(UUID id, TicketDto ticketDto) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        existingTicket.setTitle(ticketDto.getTitle());
        existingTicket.setDescription(ticketDto.getDescription());
        existingTicket.setStatus(ticketDto.getStatus());
        existingTicket.setPriority(ticketDto.getPriority());
        existingTicket.setEmployeeId(ticketDto.getEmployeeId());
        existingTicket.setClientEmail(ticketDto.getClientEmail());
        Ticket updatedTicket = ticketRepository.save(existingTicket);

        return convertToDto(updatedTicket);
    }

    @Override
    public void deleteTicket(UUID id) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        ticketRepository.delete(existingTicket);
    }

    @Override
    public List<TicketDto> getAllTickets() {
        System.out.println("Fetching all tickets from the database...");

        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(this::convertToDto)
                .toList();
    }

    private TicketDto convertToDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getEmployeeId(),
                ticket.getClientEmail()
        );
    }

    private Ticket convertToEntity(TicketDto ticketDto) {
        Ticket ticket = new Ticket();

        ticket.setId(ticketDto.getId());
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setPriority(ticketDto.getPriority());
        ticket.setEmployeeId(ticketDto.getEmployeeId());
        ticket.setClientEmail(ticketDto.getClientEmail());
        return ticket;
    }
}