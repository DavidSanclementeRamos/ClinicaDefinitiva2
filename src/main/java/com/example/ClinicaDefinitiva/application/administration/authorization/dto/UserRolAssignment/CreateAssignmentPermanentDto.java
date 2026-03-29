package com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment;

public record CreateAssignmentPermanentDto(
        Long userId,
        Long rolId,
        boolean isPrimary

) {
}
