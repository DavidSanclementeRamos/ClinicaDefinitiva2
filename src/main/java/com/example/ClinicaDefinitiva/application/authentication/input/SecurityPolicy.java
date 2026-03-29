package com.example.ClinicaDefinitiva.application.authentication.input;

import java.time.Duration;

public interface SecurityPolicy {
    int getMaxAttempts();
    Duration getLockDuration();
}
