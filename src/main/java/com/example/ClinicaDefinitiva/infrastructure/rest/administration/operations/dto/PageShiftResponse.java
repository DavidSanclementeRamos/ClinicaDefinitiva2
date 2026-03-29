// PageShiftResponse.java
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record PageShiftResponse(
    Long shiftId,
    Long dentistId,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String type,
    String status
) {}
