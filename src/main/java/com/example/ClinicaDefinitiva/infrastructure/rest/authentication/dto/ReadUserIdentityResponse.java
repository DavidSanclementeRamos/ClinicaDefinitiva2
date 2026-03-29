package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto;

import java.time.Instant;

public record ReadUserIdentityResponse(
        Long id,
        String email,
        String name,
        Instant createdAt,
        Instant lastLoginAt,
        int failedLoginAttempts,
        Instant lockedUntil,
        boolean verified,
        String status
) {
}
