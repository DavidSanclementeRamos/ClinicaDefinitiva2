package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public  record   DentistId (Long value){

    public static DentistId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.ACTORS);
        }

        return new DentistId(value);
    }

}
