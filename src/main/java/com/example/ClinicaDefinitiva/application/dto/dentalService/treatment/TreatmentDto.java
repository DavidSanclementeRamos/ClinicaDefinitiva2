package com.example.ClinicaDefinitiva.application.dto.dentalService.treatment;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para lectura de un tratamiento.
 * Contiene todos los detalles del tratamiento, incluyendo fechas y estado.
 */
public record TreatmentDto(
        Long id,
        Long patientId,
        Long dentistId,
        Long serviceId,
        String status,
        LocalDate startDate,
        LocalDate expectedEndDate,
        LocalDate actualEndDate,
        List<TreatmentPhaseDto> phases,
        String notes,
        Long rateId
) {}