package com.serviciotecnico.cotizacion.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.cotizacion.dto.CotizacionRequest;
import com.serviciotecnico.cotizacion.dto.CotizacionResponse;
import com.serviciotecnico.cotizacion.service.CotizacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @GetMapping
    public ResponseEntity<List<CotizacionResponse>> listarTodas() {
        return ResponseEntity.ok(cotizacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cotizacionService.obtenerPorId(id));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<CotizacionResponse>> listarPorTicket(
            @PathVariable UUID ticketId
    ) {
        return ResponseEntity.ok(cotizacionService.listarPorTicket(ticketId));
    }

    @PostMapping
    public ResponseEntity<CotizacionResponse> crear(
            @Valid @RequestBody CotizacionRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization
    ) {
        CotizacionResponse response = cotizacionService.crear(request, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CotizacionResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CotizacionRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization
    ) {
        return ResponseEntity.ok(
                cotizacionService.actualizar(id, request, authorization)
        );
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<CotizacionResponse> aprobar(
            @PathVariable UUID id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization
    ) {
        return ResponseEntity.ok(cotizacionService.aprobar(id, authorization));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<CotizacionResponse> rechazar(@PathVariable UUID id) {
        return ResponseEntity.ok(cotizacionService.rechazar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cotizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
