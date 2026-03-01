
package com.example.ClinicaDefinitiva.application.dto.administration.operations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO de lectura para turnos operativos.
 * Representa el estado completo de un turno.
 */
public record ReadShiftDto(
        Long id,
        Long dentistId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String type,
        String status,
        String cancellationReason,
        List<ExcludedBlockDto> excludedBlocks,
        Long version
) {}











