package com.example.ClinicaDefinitiva.application.portsInput.authentication;

import java.time.Duration;

public interface SecurityPolicy {
    int getMaxAttempts();
    Duration getLockDuration();
}
