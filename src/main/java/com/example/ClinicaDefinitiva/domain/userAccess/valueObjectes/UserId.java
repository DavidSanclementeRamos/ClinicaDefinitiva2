package com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public class UserId {
    private final Long value;

    public UserId(Long value) {
        this.value = value;
    }


    // Nuevo: parsea/valida una Long y devuelve el VO
    public static UserId from(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoAccesError.ERR_USER_ID_INVALID, VOContext.USER_ID);
        }

       return new UserId(value);
    }
    public Long getValue() {
        return value;
    }
}
