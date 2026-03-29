package com.example.ClinicaDefinitiva.application.clinicalTreatments.dto;

import java.time.LocalDate;

/**
 * DTO para representar una fase del tratamiento.
 */
public record TreatmentPhaseDto(
        String name,
        LocalDate startDate,
        String status,
        String description
) {}
