package com.example.ClinicaDefinitiva.infrastructure.security.config;

import com.example.ClinicaDefinitiva.application.portsInput.userIdentity.SecurityPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ConfigurableSecurityPolicy implements SecurityPolicy {
    private final int maxAttempts;
    private final Duration lockDuration;

    public ConfigurableSecurityPolicy(
            @Value("${security.maxAttempts}") int maxAttempts,
            @Value("${security.lockDurationMinutes}") long lockDurationMinutes
    ) {
        this.maxAttempts = maxAttempts;
        this.lockDuration = Duration.ofMinutes(lockDurationMinutes);
    }

    @Override
    public int getMaxAttempts() { return maxAttempts; }

    @Override
    public Duration getLockDuration() { return lockDuration; }
}

