/**
 * Controlador REST encargado de administrar las operaciones sobre clientes.
 *
 * <p>Expone endpoints para listar, buscar, crear, actualizar
 * y eliminar clientes dentro del sistema.</p>
 */
package com.serviciotecnico.cliente.controller;

import java.util.List;

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

import com.serviciotecnico.cliente.dto.ClienteDto;
import com.serviciotecnico.cliente.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{email}")
    public ResponseEntity<ClienteDto> getClienteByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clienteService.getClienteByEmail(email));
    }

    @PostMapping
    public ResponseEntity<ClienteDto> createCliente(@RequestBody ClienteDto clienteDto) {
        ClienteDto createdCliente = clienteService.createCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCliente);
    }

    @PutMapping("/{email}")
    public ResponseEntity<ClienteDto> updateCliente(@PathVariable String email, @RequestBody ClienteDto clienteDto) {
        return ResponseEntity.ok(clienteService.updateCliente(email, clienteDto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String email) {
        clienteService.deleteCliente(email);
        return ResponseEntity.noContent().build();
    }
}
