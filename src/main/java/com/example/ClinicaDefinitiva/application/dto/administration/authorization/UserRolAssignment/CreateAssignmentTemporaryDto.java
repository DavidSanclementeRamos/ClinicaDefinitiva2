package com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment;

import java.time.LocalDate;

public record CreateAssignmentTemporaryDto(
        Long userId,
        Long rolId,
        boolean isPrimary,
        LocalDate validFrom,
        LocalDate validTo
) {
}
