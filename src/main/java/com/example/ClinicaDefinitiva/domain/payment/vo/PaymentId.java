
package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;


public record PaymentId(Long value) {
     public static PaymentId of(Long value) {
         if (value == null) {
            throw new ValueObjectValidationException(PaymentVoError.ERR_PAYMENT_ID_NULL, VOContext.PAYMENT);
        }
        return new PaymentId(value);
    }
    
}
