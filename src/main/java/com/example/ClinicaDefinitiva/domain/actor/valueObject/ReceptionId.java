package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;


public final class ReceptionId {
private final Long value;

    public ReceptionId(Long value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ReceptionId fromLong(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.RECEPTION_ID);

        }

        return new ReceptionId(value);
    }
    public Long getValue() {
        return value;
    }
}
