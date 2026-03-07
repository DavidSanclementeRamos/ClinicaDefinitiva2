package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


public record  InvoiceId(Long getValue) {



    public static InvoiceId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ID_NULL, VOContext.BILLING);
        }
       return new InvoiceId(value);
    }


}
