package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public record  ContractId(Long getValue ) {
    

    public static ContractId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_CONTRACT_ID_NULL,
                    VOContext.ACCOUNTING
            );}
        return new ContractId(value);
    }

}



