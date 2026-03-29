package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;

import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import java.util.Objects;

public final class DentistAvailabilityStatus {

    public enum Status {
        AVAILABLE("Disponible"),
        SICK_LEAVE("Incapacidad médica"),
        VACATION("Ausencia planificada");

        private final String description;

        Status(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    private final Status value;

    private DentistAvailabilityStatus(Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_DENTIST_STATUS_NULL,
                    VOContext.ACTORS
            );
        }
        this.value = value;
    }

    public static DentistAvailabilityStatus of(Status status) {
        return new DentistAvailabilityStatus(status);
    }

    // Queries semánticas
    public boolean isAvailable() { return value == Status.AVAILABLE; }
    public boolean isAbsent() { return value == Status.SICK_LEAVE || value == Status.VACATION; }

    // Reglas de negocio
    public enum Priority { NOT_ASSIGNABLE, HIGH }
    public Priority getPriority() {
        return isAvailable() ? Priority.HIGH : Priority.NOT_ASSIGNABLE;
    }

    public String getDescription() { return value.getDescription(); }
    public Status getValue() { return value; }

    @Override
    public String toString() { return value.name(); }
    
    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DentistAvailabilityStatus)) return false;
    DentistAvailabilityStatus that = (DentistAvailabilityStatus) o;
    return this.value == that.value;
}

@Override
public int hashCode() {
    return Objects.hash(value);
}
}