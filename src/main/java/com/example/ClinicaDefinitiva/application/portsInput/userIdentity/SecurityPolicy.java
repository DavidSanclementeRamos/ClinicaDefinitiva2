package com.example.ClinicaDefinitiva.application.portsInput.userIdentity;

import java.time.Duration;

public interface SecurityPolicy {
    int getMaxAttempts();
    Duration getLockDuration();
}
