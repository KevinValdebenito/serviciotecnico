package com.serviciotecnico.ticket.service;

import java.util.List;
import java.util.UUID;

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
    public List<TicketDto> searchTickets(String titulo, String estado, String prioridad, UUID empleadoId) {
        return ticketRepository.findAll().stream()
                .filter(ticket -> titulo == null || titulo.isBlank()
                        || ticket.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .filter(ticket -> estado == null || estado.isBlank()
                        || ticket.getEstado().equalsIgnoreCase(estado))
                .filter(ticket -> prioridad == null || prioridad.isBlank()
                        || ticket.getPrioridad().equalsIgnoreCase(prioridad))
                .filter(ticket -> empleadoId == null
                        || ticket.getEmpleadoId().equals(empleadoId))
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

        existingTicket.setTitulo(ticketDto.getTitulo());
        existingTicket.setDescripcion(ticketDto.getDescripcion());
        existingTicket.setEstado(ticketDto.getEstado());
        existingTicket.setPrioridad(ticketDto.getPrioridad());
        existingTicket.setEmpleadoId(ticketDto.getEmpleadoId());

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
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getEstado(),
                ticket.getPrioridad(),
                ticket.getEmpleadoId()
        );
    }

    private Ticket convertToEntity(TicketDto ticketDto) {
        Ticket ticket = new Ticket();

        ticket.setId(ticketDto.getId());
        ticket.setTitulo(ticketDto.getTitulo());
        ticket.setDescripcion(ticketDto.getDescripcion());
        ticket.setEstado(ticketDto.getEstado());
        ticket.setPrioridad(ticketDto.getPrioridad());
        ticket.setEmpleadoId(ticketDto.getEmpleadoId());

        return ticket;
    }
}