package com.serviciotecnico.cotizacion.exception;

/**
 * Error producido al comunicarse con otro microservicio.
 */
public class RemoteServiceException extends RuntimeException {

    public RemoteServiceException(String message) {
        super(message);
    }
}
