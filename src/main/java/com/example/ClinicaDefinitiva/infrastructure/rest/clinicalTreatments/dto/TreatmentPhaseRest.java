package com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.dto;

import java.time.LocalDate;

public record TreatmentPhaseRest(
        String name,
        LocalDate startDate,
        String status,
        String description
) {
}
