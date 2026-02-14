package com.example.ClinicaDefinitiva.application.dto.dentalService.treatment;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear un nuevo tratamiento.
 * Contiene toda la información requerida para inicializar un tratamiento clínico.
 */
public record CreateTreatmentDto(
        String patientId,
        String dentistId,
        String serviceId,
        LocalDate startDate,
        LocalDate expectedEndDate,
        List<TreatmentPhaseDto> phases,
        String notes,
        String rateId
) {}




