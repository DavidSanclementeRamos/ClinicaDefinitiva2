package com.example.ClinicaDefinitiva.application.clinicalTreatments.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear un nuevo tratamiento.
 * Contiene toda la información requerida para inicializar un tratamiento clínico.
 */
public record CreateTreatmentDto(
        Long patientId,
        Long dentistId,
        Long serviceId,
        LocalDate startDate,
        LocalDate expectedEndDate,
        List<TreatmentPhaseDto> phases,
        String notes,
        Long rateId
) {}




