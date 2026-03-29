package com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment;

import java.time.LocalDate;

public record CreateAssignmentTemporaryDto(
        Long userId,
        Long rolId,
        boolean isPrimary,
        LocalDate validFrom,
        LocalDate validTo
) {
}
