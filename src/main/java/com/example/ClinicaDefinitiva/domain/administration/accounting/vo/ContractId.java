package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ContractId {

    private final Long value;

    private ContractId(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_CONTRACT_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_CONTRACT_ID_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static ContractId of(Long value) {
        return new ContractId(value);
    }

    public Long getValue() { return value; }
}



