/**
 * Controlador REST encargado de administrar el inventario de repuestos.
 *
 * <p>Expone endpoints para listar, buscar, crear, actualizar, eliminar
 * y ajustar el stock de los repuestos disponibles.</p>
 */
package com.serviciotecnico.repuesto.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.repuesto.dto.AjusteStockRequest;
import com.serviciotecnico.repuesto.dto.RepuestoDto;
import com.serviciotecnico.repuesto.service.RepuestoService;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    @GetMapping
    public ResponseEntity<List<RepuestoDto>> getAllRepuestos() {
        return ResponseEntity.ok(repuestoService.getAllRepuestos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoDto> getRepuestoById(@PathVariable UUID id) {
        return ResponseEntity.ok(repuestoService.getRepuestoById(id));
    }

    @PostMapping
    public ResponseEntity<RepuestoDto> createRepuesto(@RequestBody RepuestoDto repuestoDto) {
        RepuestoDto createdRepuesto = repuestoService.createRepuesto(repuestoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRepuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoDto> updateRepuesto(@PathVariable UUID id, @RequestBody RepuestoDto repuestoDto) {
        return ResponseEntity.ok(repuestoService.updateRepuesto(id, repuestoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepuesto(@PathVariable UUID id) {
        repuestoService.deleteRepuesto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reducir-stock")
    public ResponseEntity<RepuestoDto> reducirStock(@PathVariable UUID id, @RequestBody AjusteStockRequest request) {
        return ResponseEntity.ok(repuestoService.reducirStock(id, request.cantidad()));
    }

    @PostMapping("/{id}/aumentar-stock")
    public ResponseEntity<RepuestoDto> aumentarStock(@PathVariable UUID id, @RequestBody AjusteStockRequest request) {
        return ResponseEntity.ok(repuestoService.aumentarStock(id, request.cantidad()));
    }
}