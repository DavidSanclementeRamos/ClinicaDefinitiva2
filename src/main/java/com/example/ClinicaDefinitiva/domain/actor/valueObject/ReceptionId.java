package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class ReceptionId {
private final String value;

    public ReceptionId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static ReceptionId generate(){
        return new ReceptionId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }
}
