package com.serviciotecnico.diagnostico.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            ex.getReason() != null ? ex.getReason() : ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .orElse("Solicitud inválida");

        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            message,
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            "Error inesperado en el servicio de diagnósticos: " + ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}