package com.example.ClinicaDefinitiva.domain.dental.care.services.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ServiceCode {
    private static final int MIN_CODE_LENGTH = 4;
    private static final int MAX_CODE_LENGTH = 15;

    private final String value;

    public ServiceCode(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_CODE_REQUIRED, VOContext.SERVICE_CODE);
        }
        if (!value.matches("^[A-Z0-9\\-]+$")) { // Ejemplo: solo mayúsculas, números y guiones
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_CODE_FORMAT_INVALID,VOContext.SERVICE_CODE);
        }
        if (value.length() < MIN_CODE_LENGTH || value.length() > MAX_CODE_LENGTH) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_CODE_LENGTH_INVALID,VOContext.SERVICE_CODE);}
        this.value = value;
    }
    public void ensureUniqueCode(boolean exists) {
        if (exists) {
            throw new ValueObjectValidationException(
                    ServiceVOError.ERR_SERVICE_CODE_DUPLICATE,
                    VOContext.SERVICE_CODE);
        }
    }


    public String getValue() { return value; }

    @Override
    public String toString() { return value; }
}


