package com.serviciotecnico.cliente.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.serviciotecnico.cliente.dto.ClienteDto;
import com.serviciotecnico.cliente.entity.Cliente;
import com.serviciotecnico.cliente.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDto> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ClienteDto getClienteByEmail(String email) {
        Cliente cliente = clienteRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
        return convertToDto(cliente);
    }

    @Override
    public ClienteDto createCliente(ClienteDto clienteDto) {
        Cliente cliente = convertToEntity(clienteDto);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDto(savedCliente);
    }

    @Override
    public ClienteDto updateCliente(String email, ClienteDto clienteDto) {
        Cliente existingCliente = clienteRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));

        existingCliente.setRut(clienteDto.rut());
        existingCliente.setNombreCompleto(clienteDto.nombreCompleto());
        existingCliente.setTelefono(clienteDto.telefono());
        existingCliente.setDireccion(clienteDto.direccion());
        
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return convertToDto(updatedCliente);
    }

    @Override
    public void deleteCliente(String email) {
        Cliente existingCliente = clienteRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
        clienteRepository.delete(existingCliente);
    }

    private ClienteDto convertToDto(Cliente cliente) {
        return new ClienteDto(
                cliente.getEmail(),
                cliente.getRut(),
                cliente.getNombreCompleto(),
                cliente.getTelefono(),
                cliente.getDireccion()
        );
    }

    private Cliente convertToEntity(ClienteDto dto) {
        return new Cliente(
                dto.email(),
                dto.rut(),
                dto.nombreCompleto(),
                dto.telefono(),
                dto.direccion()
        );
    }
}