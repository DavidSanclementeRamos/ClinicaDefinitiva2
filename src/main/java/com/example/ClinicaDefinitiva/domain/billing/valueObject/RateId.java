package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public record  RateId(Long getValue) {

    
    public static RateId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_RATE_ID_NULL, VOContext.BILLING);
        }

        return new RateId(value);
    }
}
