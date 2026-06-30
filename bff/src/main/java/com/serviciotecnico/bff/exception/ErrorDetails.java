package com.serviciotecnico.bff.exception;

import java.time.LocalDateTime;

public record ErrorDetails(
    LocalDateTime timestamp,
    String message,
    String details
) {}