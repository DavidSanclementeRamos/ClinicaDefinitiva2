package com.example.ClinicaDefinitiva.domain.billing.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public  record InvoiceItemId(Long getValue) {

    public static InvoiceItemId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ITEM_ID_NULL, VOContext.BILLING);
        }
       return new InvoiceItemId(value);
    }

}
