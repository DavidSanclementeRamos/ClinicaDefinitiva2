package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class LedgerAccountId {

    private final Long value;

    private LedgerAccountId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_LEDGER_ACCOUNT_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_LEDGER_ACCOUNT_ID_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static LedgerAccountId of(Long value) {
        return new LedgerAccountId(value);
    }

    public Long getValue() { return value; }
}
