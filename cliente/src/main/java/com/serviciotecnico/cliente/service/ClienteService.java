/**
 * Interfaz que define las operaciones disponibles para la gestión de clientes.
 *
 * <p>Permite desacoplar la lógica de negocio de su implementación concreta.</p>
 */
package com.serviciotecnico.cliente.service;

import java.util.List;

import com.serviciotecnico.cliente.dto.ClienteDto;

public interface ClienteService {
    List<ClienteDto> getAllClientes();
    ClienteDto getClienteByEmail(String email);
    ClienteDto createCliente(ClienteDto clienteDto);
    ClienteDto updateCliente(String email, ClienteDto clienteDto);
    void deleteCliente(String email);
}
