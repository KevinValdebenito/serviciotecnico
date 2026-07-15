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

import com.serviciotecnico.cotizacion.dto.AjusteStockRequest;
import com.serviciotecnico.cotizacion.dto.RepuestoResponse;
import com.serviciotecnico.cotizacion.exception.AutorizacionException;
import com.serviciotecnico.cotizacion.exception.RemoteServiceException;
import com.serviciotecnico.cotizacion.exception.ResourceNotFoundException;
import com.serviciotecnico.cotizacion.exception.StockException;

@Component
public class RepuestoClientImpl implements RepuestoClient {

    private static final Logger log = LoggerFactory.getLogger(RepuestoClientImpl.class);

    private final RestClient restClient;

    public RepuestoClientImpl(@Value("${repuesto.service.url}") String repuestoServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(repuestoServiceUrl)
                .build();
    }

    @Override
    public RepuestoResponse obtenerPorId(UUID repuestoId, String authorization) {
        validarAuthorization(authorization);

        try {
            RepuestoResponse response = restClient.get()
                    .uri("/api/repuestos/{id}", repuestoId)
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .retrieve()
                    .body(RepuestoResponse.class);

            return validarRespuesta(response);
        } catch (RestClientResponseException ex) {
            throw convertirError(ex, repuestoId);
        } catch (RestClientException ex) {
            log.error("No fue posible conectar con el microservicio repuesto", ex);
            throw new RemoteServiceException("El microservicio de repuestos no está disponible");
        }
    }

    @Override
    public RepuestoResponse reducirStock(
            UUID repuestoId,
            Integer cantidad,
            String authorization
    ) {
        return ajustarStock(repuestoId, cantidad, "reducir-stock", authorization);
    }

    @Override
    public RepuestoResponse aumentarStock(
            UUID repuestoId,
            Integer cantidad,
            String authorization
    ) {
        return ajustarStock(repuestoId, cantidad, "aumentar-stock", authorization);
    }

    private RepuestoResponse ajustarStock(
            UUID repuestoId,
            Integer cantidad,
            String operacion,
            String authorization
    ) {
        validarAuthorization(authorization);

        try {
            RepuestoResponse response = restClient.post()
                    .uri("/api/repuestos/{id}/{operacion}", repuestoId, operacion)
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .body(new AjusteStockRequest(cantidad))
                    .retrieve()
                    .body(RepuestoResponse.class);

            return validarRespuesta(response);
        } catch (RestClientResponseException ex) {
            throw convertirError(ex, repuestoId);
        } catch (RestClientException ex) {
            log.error("No fue posible ajustar el stock del repuesto {}", repuestoId, ex);
            throw new RemoteServiceException("El microservicio de repuestos no está disponible");
        }
    }

    private RepuestoResponse validarRespuesta(RepuestoResponse response) {
        if (response == null || response.id() == null) {
            throw new RemoteServiceException(
                    "El microservicio de repuestos respondió sin datos válidos");
        }
        return response;
    }

    private RuntimeException convertirError(
            RestClientResponseException ex,
            UUID repuestoId
    ) {
        HttpStatusCode status = ex.getStatusCode();

        if (status.value() == 404) {
            return new ResourceNotFoundException(
                    "No existe el repuesto con id: " + repuestoId);
        }

        if (status.value() == 401 || status.value() == 403) {
            return new AutorizacionException(
                    "El token no permite consultar el microservicio de repuestos");
        }

        if (status.value() == 409) {
            return new StockException(
                    "El microservicio de repuestos rechazó la operación por stock insuficiente");
        }

        log.error(
                "Error remoto de repuesto. id={}, status={}",
                repuestoId,
                status,
                ex
        );
        return new RemoteServiceException(
                "Error al consultar repuestos. Código remoto: " + status.value());
    }

    private void validarAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AutorizacionException(
                    "Debes enviar el encabezado Authorization con un token Bearer");
        }
    }
}
