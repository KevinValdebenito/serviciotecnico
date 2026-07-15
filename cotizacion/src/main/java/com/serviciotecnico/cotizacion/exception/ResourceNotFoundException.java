package com.serviciotecnico.cotizacion.exception;

/**
 * Error utilizado cuando no se encuentra un recurso.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
