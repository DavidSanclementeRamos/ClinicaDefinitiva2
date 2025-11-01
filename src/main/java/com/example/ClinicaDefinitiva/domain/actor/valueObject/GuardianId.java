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

    public String getVauel() {
        return vauel;
    }
}
