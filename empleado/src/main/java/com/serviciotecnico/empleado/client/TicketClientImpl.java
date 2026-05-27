package com.serviciotecnico.empleado.client;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.serviciotecnico.empleado.dto.TicketDto;

@Component
public class TicketClientImpl implements TicketClient {

    private final RestClient restClient;

    public TicketClientImpl(@Value("${apiTicket.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public List<TicketDto> getAllTickets() {
        ResponseEntity<List<TicketDto>> response = restClient.get()
                .uri("/tickets")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TicketDto>>() {});
        List<TicketDto> body = response.getBody();
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful() && body != null) {
            return body;
        } else {
            throw new RuntimeException("Failed to fetch tickets: " + statusCode);
        }
    }

    @Override
    public List<TicketDto> searchTickets(String name, String status) {
        ResponseEntity<List<TicketDto>> response = restClient.get().uri(uriBuilder -> {
            var builder = uriBuilder.path("/tickets");
            if (name != null && !name.isBlank()) {
                builder.queryParam("name", name);
            }
            if (status != null && !status.isBlank()) {
                builder.queryParam("status", status);
            }

            return builder.build();
        })
        .retrieve()
        .toEntity(new ParameterizedTypeReference<List<TicketDto>>() {
        });

        List<TicketDto> body = response.getBody();
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful() && body != null) {
            return body;
        }
        throw new RuntimeException("Failed to search Tickets: " + statusCode);
    }
    
    @Override
    public TicketDto getTicketById(UUID id){
        TicketDto ticket = restClient.get()
                .uri("/tickets/{id}", id)
                .retrieve()
                .body(TicketDto.class);
        return ticket;
    }

    @Override
    public TicketDto createTicket(TicketDto ticketDto){
        ResponseEntity<TicketDto> response = restClient.post()
                .uri("/tickets")
                .body(ticketDto)
                .retrieve()
                .toEntity(TicketDto.class);
        TicketDto body = response.getBody();
        HttpStatusCode statusCode = response.getStatusCode();
        if ((statusCode.is2xxSuccessful() || statusCode.value() == HttpStatus.CREATED.value()) && body != null) {
            return body;
        } 
        throw new RuntimeException("Failed to create ticket: " + statusCode); 
    }

    @Override
    public TicketDto updateTicket(UUID id, TicketDto ticketDto){
        ResponseEntity<TicketDto> response = restClient.put()
                .uri("/tickets/{id}", id)
                .body(ticketDto)
                .retrieve()
                .toEntity(TicketDto.class);
        TicketDto body = response.getBody();
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful() && body != null) {
            return body;
        }
        throw new RuntimeException("Failed to update ticket: " + statusCode);
    }

    @Override
    public void deleteTicket(UUID id) {
        ResponseEntity<Void> response = restClient.delete()
                .uri("/tickets/{id}", id)
                .retrieve()
                .toBodilessEntity();
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()){
            throw new RuntimeException("Failed to delete ticket: " + statusCode);
        }
    }

}
