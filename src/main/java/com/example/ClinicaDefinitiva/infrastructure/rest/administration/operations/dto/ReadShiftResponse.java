// ReadShiftResponse.java
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReadShiftResponse(
    Long shiftId,
    Long dentistId,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String type,
    String status,
    String cancellationReason,
    List<ExcludedBlockResponse> excludedBlocks,
    Long version
) {}