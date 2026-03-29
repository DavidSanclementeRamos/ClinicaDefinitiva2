package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


public record  CompanyId(Long value) {

    public static CompanyId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_COMPANY_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        return new CompanyId(value);
    }

}

