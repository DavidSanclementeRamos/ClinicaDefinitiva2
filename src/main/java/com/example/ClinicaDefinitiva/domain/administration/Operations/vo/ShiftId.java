package com.example.ClinicaDefinitiva.domain.administration.operations.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;


public record  ShiftId(Long value)   {

    public static ShiftId from(Long value) {

        if (value == null) {
            throw new ValueObjectValidationException(OperationsVoError.ERR_SHIFT_ID_REQUIRED, VOContext.OPERATIONS);
        }

        return new ShiftId(value) ;
    }
}
