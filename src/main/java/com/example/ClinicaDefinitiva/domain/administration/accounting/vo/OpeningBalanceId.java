package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public record  OpeningBalanceId(Long getValue) {


    public static OpeningBalanceId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_OPENING_BALANCE_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        return new OpeningBalanceId(value);
    }

}
