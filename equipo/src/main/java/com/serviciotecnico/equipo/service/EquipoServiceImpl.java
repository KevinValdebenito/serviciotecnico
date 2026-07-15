/**
 * Implementación del servicio de equipos.
 *
 * <p>Contiene la lógica para consultar, crear, actualizar y eliminar equipos
 * asociados a los clientes, además de convertir entre entidades JPA y DTOs.</p>
 */
package com.serviciotecnico.equipo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.equipo.dto.EquipoDto;
import com.serviciotecnico.equipo.entity.Equipo;
import com.serviciotecnico.equipo.repository.EquipoRepository;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    /**
     * Retorna todos los equipos registrados, o solo los de un cliente
     * específico si se entrega su email.
     *
     * @param clienteEmail email del cliente a filtrar, o null para traer todos
     * @return lista de equipos encontrados
     */
    @Override
    public List<EquipoDto> getAllEquipos(String clienteEmail) {
        List<Equipo> equipos = (clienteEmail != null && !clienteEmail.isBlank())
                ? equipoRepository.findByClienteEmail(clienteEmail)
                : equipoRepository.findAll();

        return equipos.stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Busca un equipo por su identificador.
     *
     * @param id identificador del equipo
     * @return el equipo encontrado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public EquipoDto getEquipoById(UUID id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado con id: " + id));
        return convertToDto(equipo);
    }

    /**
     * Registra un nuevo equipo en el sistema.
     *
     * @param equipoDto datos del equipo a crear
     * @return el equipo creado, con su id generado
     */
    @Override
    public EquipoDto createEquipo(EquipoDto equipoDto) {
        Equipo equipo = convertToEntity(equipoDto);
        Equipo savedEquipo = equipoRepository.save(equipo);
        return convertToDto(savedEquipo);
    }

    /**
     * Actualiza los datos de un equipo existente.
     *
     * @param id identificador del equipo a actualizar
     * @param equipoDto nuevos datos del equipo
     * @return el equipo actualizado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public EquipoDto updateEquipo(UUID id, EquipoDto equipoDto) {
        Equipo existingEquipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado con id: " + id));

        existingEquipo.setClienteEmail(equipoDto.clienteEmail());
        existingEquipo.setTipo(equipoDto.tipo());
        existingEquipo.setMarca(equipoDto.marca());
        existingEquipo.setModelo(equipoDto.modelo());
        existingEquipo.setNumeroSerie(equipoDto.numeroSerie());

        Equipo updatedEquipo = equipoRepository.save(existingEquipo);
        return convertToDto(updatedEquipo);
    }

    /**
     * Elimina un equipo del sistema.
     *
     * @param id identificador del equipo a eliminar
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public void deleteEquipo(UUID id) {
        Equipo existingEquipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado con id: " + id));
        equipoRepository.delete(existingEquipo);
    }

    private EquipoDto convertToDto(Equipo equipo) {
        return new EquipoDto(
                equipo.getId(),
                equipo.getClienteEmail(),
                equipo.getTipo(),
                equipo.getMarca(),
                equipo.getModelo(),
                equipo.getNumeroSerie()
        );
    }

    private Equipo convertToEntity(EquipoDto dto) {
        return new Equipo(
                dto.clienteEmail(),
                dto.tipo(),
                dto.marca(),
                dto.modelo(),
                dto.numeroSerie()
        );
    }
}