/**
 * Controlador REST encargado de administrar los diagnósticos técnicos.
 *
 * <p>Expone endpoints para listar, buscar, crear, actualizar
 * y eliminar diagnósticos asociados a los tickets.</p>
 */
package com.serviciotecnico.diagnostico.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serviciotecnico.diagnostico.dto.DiagnosticoDto;
import com.serviciotecnico.diagnostico.service.DiagnosticoService;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping
    public ResponseEntity<List<DiagnosticoDto>> getAllDiagnosticos(@RequestParam(required = false) UUID ticketId) {
        return ResponseEntity.ok(diagnosticoService.getAllDiagnosticos(ticketId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDto> getDiagnosticoById(@PathVariable UUID id) {
        return ResponseEntity.ok(diagnosticoService.getDiagnosticoById(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoDto> createDiagnostico(@RequestBody DiagnosticoDto diagnosticoDto) {
        DiagnosticoDto createdDiagnostico = diagnosticoService.createDiagnostico(diagnosticoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiagnostico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDto> updateDiagnostico(@PathVariable UUID id, @RequestBody DiagnosticoDto diagnosticoDto) {
        return ResponseEntity.ok(diagnosticoService.updateDiagnostico(id, diagnosticoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnostico(@PathVariable UUID id) {
        diagnosticoService.deleteDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}