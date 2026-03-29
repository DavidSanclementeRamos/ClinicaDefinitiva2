package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


public record  ReceptionId(Long getValue) {



    public static ReceptionId of(Long getValue) {
        if (getValue == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_ID_NULL, VOContext.ACTORS);
        }
        return new ReceptionId(getValue);
    }

}
