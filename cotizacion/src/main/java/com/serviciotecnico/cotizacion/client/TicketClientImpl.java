package com.serviciotecnico.cotizacion.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.serviciotecnico.cotizacion.dto.TicketResponse;
import com.serviciotecnico.cotizacion.exception.AutorizacionException;
import com.serviciotecnico.cotizacion.exception.RemoteServiceException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;

@Component
public class TicketClientImpl implements TicketClient {

    private static final Logger log = LoggerFactory.getLogger(TicketClientImpl.class);

    private final RestClient restClient;

    public TicketClientImpl(@Value("${ticket.service.url}") String ticketServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(ticketServiceUrl)
                .build();
    }

    @Override
    public TicketResponse obtenerPorId(UUID ticketId, String authorization) {
        validarAuthorization(authorization);

        try {
            TicketResponse response = restClient.get()
                    .uri("/api/tickets/{id}", ticketId)
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .retrieve()
                    .body(TicketResponse.class);

            if (response == null || response.id() == null) {
                throw new RemoteServiceException(
                        "El microservicio de tickets respondió sin datos válidos");
            }

            return response;
        } catch (RestClientResponseException ex) {
            throw convertirError(ex, ticketId);
        } catch (RestClientException ex) {
            log.error("No fue posible conectar con el microservicio ticket", ex);
            throw new RemoteServiceException(
                    "El microservicio de tickets no está disponible");
        }
    }

    private RuntimeException convertirError(
            RestClientResponseException ex,
            UUID ticketId
    ) {
        HttpStatusCode status = ex.getStatusCode();

        if (status.value() == 404) {
            return new ResourceNotFoundException(
                    "No existe el ticket con id: " + ticketId);
        }

        if (status.value() == 401 || status.value() == 403) {
            return new AutorizacionException(
                    "El token no permite consultar el microservicio de tickets");
        }

        log.error(
                "Error remoto de ticket. id={}, status={}",
                ticketId,
                status,
                ex
        );
        return new RemoteServiceException(
                "Error al consultar tickets. Código remoto: " + status.value());
    }

    private void validarAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AutorizacionException(
                    "Debes enviar el encabezado Authorization con un token Bearer");
        }
    }
}
