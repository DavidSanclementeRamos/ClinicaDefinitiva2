package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService;

import java.time.LocalDate;

public record TreatmentPhaseRest(
        String name,
        LocalDate startDate,
        String status,
        String description
) {
}
