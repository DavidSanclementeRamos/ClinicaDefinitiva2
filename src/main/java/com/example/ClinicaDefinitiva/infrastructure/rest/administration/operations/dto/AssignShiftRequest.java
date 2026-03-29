// AssignShiftRequest.java
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AssignShiftRequest(
    @NotNull(message = "Dentist ID is required")
    Long dentistId,
    
    @NotNull(message = "Date is required")
    LocalDate date,
    
    @NotNull(message = "Start time is required")
    LocalTime startTime,
    
    @NotNull(message = "End time is required")
    LocalTime endTime,
    
    @NotBlank(message = "Shift type is required")
    String type
) {}
