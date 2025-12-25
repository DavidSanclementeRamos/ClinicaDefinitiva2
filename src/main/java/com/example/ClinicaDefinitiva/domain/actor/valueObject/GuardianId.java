package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public final class GuardianId {
    private final  String value;

    public GuardianId(String vauel) {
        this.value = Objects.requireNonNull(vauel);
    }
    public static GuardianId generate(){
        return new GuardianId(UUID.randomUUID().toString());
    }
    public static GuardianId fromString(String value) {
        if (value == null)   {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_NULL, VOContext.GUARDIAN_ID);
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty())  {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_BLANK, VOContext.GUARDIAN_ID);
        }

        return new GuardianId(trimmed);
    }

    public String getValue() {
        return value;
    }
}
