package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment;

import java.time.LocalDate;

public record ReadAssignmentResponse(
        Long id,
        Long userId,
        Long rolId,
        boolean isPrimary,
        LocalDate validFrom,
        LocalDate validTo
) {
}
