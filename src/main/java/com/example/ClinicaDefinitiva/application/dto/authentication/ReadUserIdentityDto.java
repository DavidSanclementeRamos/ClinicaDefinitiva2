package com.example.ClinicaDefinitiva.application.dto.authentication;

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
        String status,
        long version
) {
}
