package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ThirdPartiesId {

    private final Long value;

    private ThirdPartiesId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_THIRDPARTIES_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_THIRDPARTIES_ID_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static ThirdPartiesId of(Long value) {
        return new ThirdPartiesId(value);
    }

    public Long getValue() { return value; }
}
