/**
 * Controlador REST encargado de administrar las operaciones sobre equipos.
 *
 * <p>Expone endpoints para listar, buscar, crear, actualizar
 * y eliminar equipos registrados por los clientes.</p>
 */
package com.serviciotecnico.equipo.controller;

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

import com.serviciotecnico.equipo.dto.EquipoDto;
import com.serviciotecnico.equipo.service.EquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<List<EquipoDto>> getAllEquipos(@RequestParam(required = false) String clienteEmail) {
        return ResponseEntity.ok(equipoService.getAllEquipos(clienteEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> getEquipoById(@PathVariable UUID id) {
        return ResponseEntity.ok(equipoService.getEquipoById(id));
    }

    @PostMapping
    public ResponseEntity<EquipoDto> createEquipo(@RequestBody EquipoDto equipoDto) {
        EquipoDto createdEquipo = equipoService.createEquipo(equipoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoDto> updateEquipo(@PathVariable UUID id, @RequestBody EquipoDto equipoDto) {
        return ResponseEntity.ok(equipoService.updateEquipo(id, equipoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable UUID id) {
        equipoService.deleteEquipo(id);
        return ResponseEntity.noContent().build();
    }
}