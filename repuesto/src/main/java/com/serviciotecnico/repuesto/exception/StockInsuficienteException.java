package com.serviciotecnico.repuesto.exception;

/**
 * Excepción de negocio lanzada cuando se intenta reducir el stock
 * de un repuesto por debajo de cero.
 */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String message) {
        super(message);
    }
}