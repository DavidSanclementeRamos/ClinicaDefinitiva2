package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ExpenseId {

    private final Long value;

    private ExpenseId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_EXPENSE_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_EXPENSE_ID_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static ExpenseId of(Long value) {
        return new ExpenseId(value);
    }

    public Long getValue() { return value; }
}

