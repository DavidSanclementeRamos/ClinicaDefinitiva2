package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;


public final class InvoiceId {
    private final Long  value;

    private InvoiceId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ID_NULL, VOContext.BILLING);
        }
        this.value = value;
    }

    public static InvoiceId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ID_NULL, VOContext.BILLING);
        }
       return new InvoiceId(value);
    }

    public Long getValue() { return value; }

}
