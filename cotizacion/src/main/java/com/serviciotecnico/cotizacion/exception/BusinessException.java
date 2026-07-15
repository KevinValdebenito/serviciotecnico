package com.serviciotecnico.cotizacion.exception;

/**
 * Error producido por una regla de negocio inválida.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
