package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

import java.time.Instant;

public record UserIdentityReadResponse(
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
