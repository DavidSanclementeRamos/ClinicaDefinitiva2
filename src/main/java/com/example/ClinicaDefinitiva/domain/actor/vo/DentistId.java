package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class  DentistId {
    private final Long value;

    private DentistId(Long value) {
        this.value = value;
    }

    // Nuevo: parsea/valida un Long y devuelve el VO
    public static DentistId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.DENTIST_ID);
        }

        return new DentistId(value);
    }

    public Long getValue() {
        return value;
    }
}
