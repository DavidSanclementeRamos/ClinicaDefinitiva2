package com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment;

import java.time.LocalDate;

public record CreateAssignmentPermanentDto(
        Long userId,
        Long rolId,
        boolean isPrimary

) {
}
