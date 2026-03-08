package com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment;

public record CreateAssignmentPermanentDto(
        Long userId,
        Long rolId,
        boolean isPrimary

) {
}
