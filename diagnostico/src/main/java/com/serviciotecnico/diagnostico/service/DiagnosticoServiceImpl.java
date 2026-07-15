/**
 * Implementación del servicio de diagnósticos técnicos.
 *
 * <p>Contiene la lógica para consultar, crear, actualizar y eliminar diagnósticos
 * asociados a los tickets, además de convertir entre entidades JPA y DTOs.</p>
 */
package com.serviciotecnico.diagnostico.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.diagnostico.dto.DiagnosticoDto;
import com.serviciotecnico.diagnostico.entity.Diagnostico;
import com.serviciotecnico.diagnostico.repository.DiagnosticoRepository;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    /**
     * Retorna todos los diagnósticos registrados, o solo los de un ticket
     * específico si se entrega su id.
     *
     * @param ticketId id del ticket a filtrar, o null para traer todos
     * @return lista de diagnósticos encontrados
     */
    @Override
    public List<DiagnosticoDto> getAllDiagnosticos(UUID ticketId) {
        List<Diagnostico> diagnosticos = (ticketId != null)
                ? diagnosticoRepository.findByTicketId(ticketId)
                : diagnosticoRepository.findAll();

        return diagnosticos.stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Busca un diagnóstico por su identificador.
     *
     * @param id identificador del diagnóstico
     * @return el diagnóstico encontrado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public DiagnosticoDto getDiagnosticoById(UUID id) {
        Diagnostico diagnostico = buscarOFallar(id);
        return convertToDto(diagnostico);
    }

    /**
     * Registra un nuevo diagnóstico técnico. La fecha se asigna automáticamente
     * en el servidor al momento de guardar.
     *
     * @param diagnosticoDto datos del diagnóstico a crear
     * @return el diagnóstico creado, con su id y fecha generados
     */
    @Override
    public DiagnosticoDto createDiagnostico(DiagnosticoDto diagnosticoDto) {
        Diagnostico diagnostico = new Diagnostico(
                diagnosticoDto.ticketId(),
                diagnosticoDto.empleadoId(),
                diagnosticoDto.descripcion()
        );
        Diagnostico savedDiagnostico = diagnosticoRepository.save(diagnostico);
        return convertToDto(savedDiagnostico);
    }

    /**
     * Actualiza la descripción de un diagnóstico existente.
     *
     * @param id identificador del diagnóstico a actualizar
     * @param diagnosticoDto nuevos datos del diagnóstico
     * @return el diagnóstico actualizado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public DiagnosticoDto updateDiagnostico(UUID id, DiagnosticoDto diagnosticoDto) {
        Diagnostico existingDiagnostico = buscarOFallar(id);

        existingDiagnostico.setDescripcion(diagnosticoDto.descripcion());
        existingDiagnostico.setEmpleadoId(diagnosticoDto.empleadoId());

        Diagnostico updatedDiagnostico = diagnosticoRepository.save(existingDiagnostico);
        return convertToDto(updatedDiagnostico);
    }

    /**
     * Elimina un diagnóstico del sistema.
     *
     * @param id identificador del diagnóstico a eliminar
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public void deleteDiagnostico(UUID id) {
        Diagnostico existingDiagnostico = buscarOFallar(id);
        diagnosticoRepository.delete(existingDiagnostico);
    }

    private Diagnostico buscarOFallar(UUID id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id: " + id));
    }

    private DiagnosticoDto convertToDto(Diagnostico diagnostico) {
        return new DiagnosticoDto(
                diagnostico.getId(),
                diagnostico.getTicketId(),
                diagnostico.getEmpleadoId(),
                diagnostico.getDescripcion(),
                diagnostico.getFecha()
        );
    }
}