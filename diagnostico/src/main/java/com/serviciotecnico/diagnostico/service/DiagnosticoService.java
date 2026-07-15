/**
 * Interfaz que define las operaciones disponibles para la gestión de diagnósticos técnicos.
 *
 * <p>Permite desacoplar la lógica de negocio de su implementación concreta.</p>
 */
package com.serviciotecnico.diagnostico.service;

import java.util.List;
import java.util.UUID;

import com.serviciotecnico.diagnostico.dto.DiagnosticoDto;

public interface DiagnosticoService {
    List<DiagnosticoDto> getAllDiagnosticos(UUID ticketId);
    DiagnosticoDto getDiagnosticoById(UUID id);
    DiagnosticoDto createDiagnostico(DiagnosticoDto diagnosticoDto);
    DiagnosticoDto updateDiagnostico(UUID id, DiagnosticoDto diagnosticoDto);
    void deleteDiagnostico(UUID id);
}