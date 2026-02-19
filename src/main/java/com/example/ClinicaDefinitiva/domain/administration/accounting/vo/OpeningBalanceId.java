package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class OpeningBalanceId {

    private final Long value;

    private OpeningBalanceId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_OPENING_BALANCE_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_OPENING_BALANCE_ID_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static OpeningBalanceId of(Long value) {
        return new OpeningBalanceId(value);
    }

    public Long getValue() { return value; }
}
