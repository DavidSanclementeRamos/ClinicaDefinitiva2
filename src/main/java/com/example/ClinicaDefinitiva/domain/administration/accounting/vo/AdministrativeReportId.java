package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Objects;

public record  AdministrativeReportId(Long value) {
       
    public static AdministrativeReportId of(Long value) {
         if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_ADMINREPORT_ID_NULL,
                    VOContext.ACCOUNTING
            );
        }
        return new AdministrativeReportId(value);
    }

}
