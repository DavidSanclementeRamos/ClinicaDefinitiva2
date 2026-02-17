package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class RateId {
    private final Long value;

    private RateId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_RATE_ID_NULL, VOContext.RATE_ID);
        }
        this.value = value;
    }
    public static RateId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_RATE_ID_NULL, VOContext.RATE_ID);
        }

        return new RateId(value);
    }
    public Long getValue() {
        return value;
    }
}
