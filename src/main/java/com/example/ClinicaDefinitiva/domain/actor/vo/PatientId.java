package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


public record  PatientId(Long value) {

   
    public static PatientId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.ACTORS);
        }
        return new PatientId(value);
    }
}
