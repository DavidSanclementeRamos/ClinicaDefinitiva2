package com.example.ClinicaDefinitiva.application.dto.user;

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
