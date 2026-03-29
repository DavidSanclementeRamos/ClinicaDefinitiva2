package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleShiftRequest(
    @NotNull(message = "New date is required")
    LocalDate newDate,
    
    @NotNull(message = "New start time is required")
    LocalTime newStart,
    
    @NotNull(message = "New end time is required")
    LocalTime newEnd,
    
    boolean hasAuthorization
) {}
