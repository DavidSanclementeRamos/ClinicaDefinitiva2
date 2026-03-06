package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public  record LedgerAccountId(Long getValue) {

    public static LedgerAccountId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_LEDGER_ACCOUNT_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        return new LedgerAccountId(value);
    }

}
