package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;


public final class PatientId {
    private final Long value;

    private PatientId(Long value) {
        this.value = value;
    }

    public static PatientId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.PATIENT_ID);

        }

        return new PatientId(value);
    }

    public Long getValue() {
        return value;
    }
}
