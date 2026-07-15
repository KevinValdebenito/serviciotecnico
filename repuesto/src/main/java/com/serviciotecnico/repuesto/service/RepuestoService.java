/**
 * Interfaz que define las operaciones disponibles para la gestión de repuestos,
 * incluyendo el control de inventario (stock).
 */
package com.serviciotecnico.repuesto.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.repuesto.dto.RepuestoDto;

public interface RepuestoService {
    List<RepuestoDto> getAllRepuestos();
    RepuestoDto getRepuestoById(UUID id);
    RepuestoDto createRepuesto(RepuestoDto repuestoDto);
    RepuestoDto updateRepuesto(UUID id, RepuestoDto repuestoDto);
    void deleteRepuesto(UUID id);
    RepuestoDto reducirStock(UUID id, Integer cantidad);
    RepuestoDto aumentarStock(UUID id, Integer cantidad);
}