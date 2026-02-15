package com.example.ClinicaDefinitiva.domain.dental.care.service.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public class TreatmentId {
    private final Long value;

    private TreatmentId(Long value) {
        this.value= value;
    }


    private static TreatmentId fromLong(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_ID_NULL, VOContext.SERVICE_ID);
        }
        return new TreatmentId(value);
    }
    public static TreatmentId of(Long value){return new TreatmentId(value);}
    public Long getValue() {
        return value;
    }
}
