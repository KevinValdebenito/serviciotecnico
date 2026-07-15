/**
 * Interfaz que define las operaciones disponibles para la gestión de equipos.
 *
 * <p>Permite desacoplar la lógica de negocio de su implementación concreta.</p>
 */
package com.serviciotecnico.equipo.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.equipo.dto.EquipoDto;

public interface EquipoService {
    List<EquipoDto> getAllEquipos(String clienteEmail);
    EquipoDto getEquipoById(UUID id);
    EquipoDto createEquipo(EquipoDto equipoDto);
    EquipoDto updateEquipo(UUID id, EquipoDto equipoDto);
    void deleteEquipo(UUID id);
}