package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments.TreatmentsVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public record TreatmentId(Long getValue) {

    public static TreatmentId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(TreatmentsVoError.ERR_TREATMENTS_ID_NULL, VOContext.CLINICAL_TREATMENTS);
        }
        return new TreatmentId(value);
    }
    
}
