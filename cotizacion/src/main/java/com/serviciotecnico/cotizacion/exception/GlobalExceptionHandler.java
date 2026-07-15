package com.serviciotecnico.cotizacion.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> manejarNegocio(
            BusinessException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(StockException.class)
    public ResponseEntity<ErrorResponse> manejarStock(
            StockException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(AutorizacionException.class)
    public ResponseEntity<ErrorResponse> manejarAutorizacion(
            AutorizacionException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(RemoteServiceException.class)
    public ResponseEntity<ErrorResponse> manejarServicioRemoto(
            RemoteServiceException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.BAD_GATEWAY, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return crearRespuesta(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Error no controlado en cotización", ex);
        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el microservicio de cotizaciones",
                request
        );
    }

    private ResponseEntity<ErrorResponse> crearRespuesta(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}
