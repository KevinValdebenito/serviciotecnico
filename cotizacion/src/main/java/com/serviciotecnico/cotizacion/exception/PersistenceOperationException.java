package com.serviciotecnico.cotizacion.exception;

/**
 * Error controlado para fallas de persistencia.
 */
public class PersistenceOperationException extends RuntimeException {

    public PersistenceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
