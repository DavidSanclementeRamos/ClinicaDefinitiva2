package com.example.ClinicaDefinitiva.application.authentication.dto;

import java.time.Instant;

public record ReadUserIdentityDto(
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
