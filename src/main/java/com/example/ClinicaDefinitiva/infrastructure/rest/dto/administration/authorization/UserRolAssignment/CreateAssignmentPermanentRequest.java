package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment;

public record CreateAssignmentPermanentRequest(
        Long userId,
        Long rolId,
        boolean isPrimary

) {
}
