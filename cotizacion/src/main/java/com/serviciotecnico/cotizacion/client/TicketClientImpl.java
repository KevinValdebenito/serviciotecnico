package com.serviciotecnico.cotizacion.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.serviciotecnico.cotizacion.dto.TicketResponse;
import com.serviciotecnico.cotizacion.exception.RemoteServiceException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;

/**
 * Cliente HTTP que consume el endpoint GET /api/tickets/{id}.
 */
@Component
public class TicketClientImpl implements TicketClient {

    private static final Logger log = LoggerFactory.getLogger(TicketClientImpl.class);

    private final RestClient restClient;

    public TicketClientImpl(@Value("${ticket.service.url}") String ticketServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(ticketServiceUrl)
                .build();
    }

    /**
     * Verifica que el ticket exista antes de crear o actualizar una cotización.
     *
     * @param ticketId identificador del ticket
     * @return ticket encontrado
     */
    @Override
    public TicketResponse obtenerPorId(UUID ticketId) {
        try {
            log.info("Consultando ticket remoto con id {}", ticketId);

            TicketResponse ticket = restClient.get()
                    .uri("/api/tickets/{id}", ticketId)
                    .retrieve()
                    .body(TicketResponse.class);

            if (ticket == null) {
                throw new RemoteServiceException(
                        "El microservicio de tickets respondió sin contenido");
            }

            return ticket;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "No existe un ticket con id: " + ticketId);
        } catch (RestClientResponseException ex) {
            log.error(
                    "El microservicio de tickets respondió con estado {}",
                    ex.getStatusCode(),
                    ex
            );
            throw new RemoteServiceException(
                    "Error al consultar tickets. Código remoto: " + ex.getStatusCode());
        } catch (RestClientException ex) {
            log.error("No fue posible conectar con el microservicio de tickets", ex);
            throw new RemoteServiceException(
                    "El microservicio de tickets no está disponible");
        }
    }
}
