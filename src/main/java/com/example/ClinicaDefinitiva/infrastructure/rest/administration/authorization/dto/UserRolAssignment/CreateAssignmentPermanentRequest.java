package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment;

public record CreateAssignmentPermanentRequest(
        Long userId,
        Long rolId,
        boolean isPrimary

) {
}
