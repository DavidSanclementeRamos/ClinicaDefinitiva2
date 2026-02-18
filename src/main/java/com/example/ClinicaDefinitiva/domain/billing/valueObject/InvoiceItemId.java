package com.example.ClinicaDefinitiva.domain.billing.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class InvoiceItemId {
    private final Long value;

   private InvoiceItemId(Long value) {
       if (value == null) {
           throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ITEM_ID_NULL, VOContext.BILLING);
       }
        this.value = value;
    }
    public static InvoiceItemId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_ITEM_ID_NULL, VOContext.BILLING);
        }
       return new InvoiceItemId(value);
    }
    public Long getValue() {
        return value;
    }

}
