package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment;

import java.time.LocalDate;

public record CreateAssignmentTemporaryRequest(
        Long userId,
        Long rolId,
        boolean isPrimary,
        LocalDate validFrom,
        LocalDate validTo
) {
}
