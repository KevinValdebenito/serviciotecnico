package com.serviciotecnico.ticket.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.UUID;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serviciotecnico.ticket.dto.TicketDto;
import com.serviciotecnico.ticket.model.Ticket;
import com.serviciotecnico.ticket.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {
    @Mock
    private TicketRepository ticketRepository;

    @Test
    void getTicketById_shouldReturnTicketDtoWhenExists() {
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID ticketId = UUID.randomUUID();
        
        Ticket mockTicket = new Ticket();
        mockTicket.setId(ticketId);
        mockTicket.setTitle("Cambio de teclado");
        mockTicket.setStatus("Pendiente");
        
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(mockTicket));

        TicketDto result = ticketService.getTicketById(ticketId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        assertEquals("Cambio de teclado", result.getTitle());
    }

    @Test
    void getTicketById_shouldThrowExceptionWhenMissing() {
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID randomId = UUID.randomUUID();
        when(ticketRepository.findById(randomId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> ticketService.getTicketById(randomId)
        );

        assertEquals("Ticket not found with id: " + randomId, exception.getMessage());
    }

    @Test
    void createTicket_shouldSaveAndReturnDto() {
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        
        TicketDto inputDto = new TicketDto();
        inputDto.setTitle("Limpieza de ventiladores");
        inputDto.setClientEmail("cliente@mail.com");

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket toSave = invocation.getArgument(0);
            toSave.setId(UUID.randomUUID()); 
            return toSave;
        });

        TicketDto result = ticketService.createTicket(inputDto);

        assertNotNull(result.getId()); 
        assertEquals("Limpieza de ventiladores", result.getTitle());

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        assertEquals("cliente@mail.com", captor.getValue().getClientEmail());
    }

    // ==========================================
    // TESTS PARA searchTickets y getAllTickets
    // ==========================================

    @Test
    void getAllTickets_shouldReturnAllMappedDtos() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        
        Ticket ticket1 = new Ticket();
        ticket1.setId(UUID.randomUUID());
        ticket1.setTitle("Ticket 1");
        
        Ticket ticket2 = new Ticket();
        ticket2.setId(UUID.randomUUID());
        ticket2.setTitle("Ticket 2");
        
        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2));

        // 2. Act
        List<TicketDto> result = ticketService.getAllTickets();

        // 3. Assert
        assertEquals(2, result.size());
        assertEquals("Ticket 1", result.get(0).getTitle());
    }

    @Test
    void searchTickets_shouldApplyFiltersCorrectly() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        
        Ticket ticketAsus = new Ticket();
        ticketAsus.setId(UUID.randomUUID());
        ticketAsus.setTitle("Cambio de pantalla Asus");
        ticketAsus.setStatus("Pendiente");
        
        Ticket ticketHP = new Ticket();
        ticketHP.setId(UUID.randomUUID());
        ticketHP.setTitle("Limpieza teclado HP");
        ticketHP.setStatus("Cerrado");
        
        when(ticketRepository.findAll()).thenReturn(List.of(ticketAsus, ticketHP));

        // 2. Act: Filtramos solo los que digan "Asus" y estén en estado "Pendiente"
        List<TicketDto> result = ticketService.searchTickets("Asus", "Pendiente", null, null);

        // 3. Assert
        assertEquals(1, result.size());
        assertEquals("Cambio de pantalla Asus", result.get(0).getTitle());
    }

    // ==========================================
    // TESTS PARA updateTicket
    // ==========================================

    @Test
    void updateTicket_shouldUpdateAndReturnDtoWhenExists() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID ticketId = UUID.randomUUID();
        
        Ticket existingTicket = new Ticket();
        existingTicket.setId(ticketId);
        existingTicket.setTitle("Titulo Viejo");

        TicketDto updateData = new TicketDto();
        updateData.setTitle("Titulo Nuevo");
        updateData.setStatus("En Curso");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0)); // Devuelve el mismo objeto que guardó

        // 2. Act
        TicketDto result = ticketService.updateTicket(ticketId, updateData);

        // 3. Assert
        assertEquals("Titulo Nuevo", result.getTitle());
        assertEquals("En Curso", result.getStatus());
    }

    @Test
    void updateTicket_shouldThrowExceptionWhenMissing() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID randomId = UUID.randomUUID();
        TicketDto updateData = new TicketDto();
        
        when(ticketRepository.findById(randomId)).thenReturn(Optional.empty());

        // 2 & 3. Act & Assert
        assertThrows(RuntimeException.class, () -> ticketService.updateTicket(randomId, updateData));
        verify(ticketRepository, never()).save(any()); // Aseguramos que nunca intentó guardarlo si no existe
    }

    // ==========================================
    // TESTS PARA deleteTicket
    // ==========================================

    @Test
    void deleteTicket_shouldDeleteWhenExists() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID ticketId = UUID.randomUUID();
        
        Ticket existingTicket = new Ticket();
        existingTicket.setId(ticketId);
        
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));

        // 2. Act
        ticketService.deleteTicket(ticketId);

        // 3. Assert
        verify(ticketRepository).delete(existingTicket); // Verificamos que se llamó al método delete del repositorio
    }

    @Test
    void deleteTicket_shouldThrowExceptionWhenMissing() {
        // 1. Arrange
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);
        UUID randomId = UUID.randomUUID();
        
        when(ticketRepository.findById(randomId)).thenReturn(Optional.empty());

        // 2 & 3. Act & Assert
        assertThrows(RuntimeException.class, () -> ticketService.deleteTicket(randomId));
        verify(ticketRepository, never()).delete(any());
    }
}