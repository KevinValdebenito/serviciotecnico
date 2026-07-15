/**
 * Implementación del servicio de repuestos.
 *
 * <p>Contiene la lógica para consultar, crear, actualizar y eliminar repuestos,
 * así como las reglas de control de stock que impiden que el inventario
 * quede en un valor negativo.</p>
 */
package com.serviciotecnico.repuesto.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.serviciotecnico.repuesto.dto.RepuestoDto;
import com.serviciotecnico.repuesto.entity.Repuesto;
import com.serviciotecnico.repuesto.exception.StockInsuficienteException;
import com.serviciotecnico.repuesto.repository.RepuestoRepository;

@Service
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    /**
     * Retorna todos los repuestos registrados en el inventario.
     *
     * @return lista completa de repuestos
     */
    @Override
    public List<RepuestoDto> getAllRepuestos() {
        return repuestoRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Busca un repuesto por su identificador.
     *
     * @param id identificador del repuesto
     * @return el repuesto encontrado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public RepuestoDto getRepuestoById(UUID id) {
        Repuesto repuesto = buscarOFallar(id);
        return convertToDto(repuesto);
    }

    /**
     * Registra un nuevo repuesto en el inventario.
     *
     * @param repuestoDto datos del repuesto a crear
     * @return el repuesto creado, con su id generado
     */
    @Override
    public RepuestoDto createRepuesto(RepuestoDto repuestoDto) {
        Repuesto repuesto = new Repuesto(
                repuestoDto.nombre(),
                repuestoDto.descripcion(),
                repuestoDto.precio(),
                repuestoDto.stock() != null ? repuestoDto.stock() : 0
        );
        Repuesto savedRepuesto = repuestoRepository.save(repuesto);
        return convertToDto(savedRepuesto);
    }

    /**
     * Actualiza los datos generales de un repuesto (nombre, descripción, precio).
     * El stock no se modifica por esta vía; usar {@link #reducirStock} o {@link #aumentarStock}.
     *
     * @param id identificador del repuesto a actualizar
     * @param repuestoDto nuevos datos del repuesto
     * @return el repuesto actualizado
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public RepuestoDto updateRepuesto(UUID id, RepuestoDto repuestoDto) {
        Repuesto existingRepuesto = buscarOFallar(id);

        existingRepuesto.setNombre(repuestoDto.nombre());
        existingRepuesto.setDescripcion(repuestoDto.descripcion());
        existingRepuesto.setPrecio(repuestoDto.precio());

        Repuesto updatedRepuesto = repuestoRepository.save(existingRepuesto);
        return convertToDto(updatedRepuesto);
    }

    /**
     * Elimina un repuesto del inventario.
     *
     * @param id identificador del repuesto a eliminar
     * @throws ResponseStatusException con estado 404 si no existe
     */
    @Override
    public void deleteRepuesto(UUID id) {
        Repuesto existingRepuesto = buscarOFallar(id);
        repuestoRepository.delete(existingRepuesto);
    }

    /**
     * Descuenta unidades del stock de un repuesto, típicamente al confirmar
     * una cotización que lo utiliza.
     *
     * @param id identificador del repuesto
     * @param cantidad unidades a descontar (debe ser mayor a 0)
     * @return el repuesto con el stock actualizado
     * @throws StockInsuficienteException si el stock resultante sería negativo
     */
    @Override
    public RepuestoDto reducirStock(UUID id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad a reducir debe ser mayor a 0");
        }

        Repuesto repuesto = buscarOFallar(id);

        if (repuesto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente para el repuesto '" + repuesto.getNombre() + "'. Disponible: " + repuesto.getStock() + ", solicitado: " + cantidad
            );
        }

        repuesto.setStock(repuesto.getStock() - cantidad);
        Repuesto updatedRepuesto = repuestoRepository.save(repuesto);
        return convertToDto(updatedRepuesto);
    }

    /**
     * Aumenta el stock de un repuesto, típicamente al reabastecer inventario.
     *
     * @param id identificador del repuesto
     * @param cantidad unidades a agregar (debe ser mayor a 0)
     * @return el repuesto con el stock actualizado
     */
    @Override
    public RepuestoDto aumentarStock(UUID id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad a agregar debe ser mayor a 0");
        }

        Repuesto repuesto = buscarOFallar(id);
        repuesto.setStock(repuesto.getStock() + cantidad);
        Repuesto updatedRepuesto = repuestoRepository.save(repuesto);
        return convertToDto(updatedRepuesto);
    }

    private Repuesto buscarOFallar(UUID id) {
        return repuestoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repuesto no encontrado con id: " + id));
    }

    private RepuestoDto convertToDto(Repuesto repuesto) {
        return new RepuestoDto(
                repuesto.getId(),
                repuesto.getNombre(),
                repuesto.getDescripcion(),
                repuesto.getPrecio(),
                repuesto.getStock()
        );
    }
}
