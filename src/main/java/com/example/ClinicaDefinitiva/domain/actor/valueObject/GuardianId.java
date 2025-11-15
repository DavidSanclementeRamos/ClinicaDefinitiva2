package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class GuardianId {
    private final  String vauel;

    public GuardianId(String vauel) {
        this.vauel = Objects.requireNonNull(vauel);
    }
    public static GuardianId generate(){
        return new GuardianId(UUID.randomUUID().toString());
    }
    public static GuardianId fromString(String value) {
        if (value == null) return null; // cambia a throw new IllegalArgumentException(...) si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("GuardianId string is empty");
        return new GuardianId(trimmed);
    }

    public String getVauel() {
        return vauel;
    }
}
