package com.example.ClinicaDefinitiva.domain.dental.care.service.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * Value Object para la descripción del servicio odontológico.
 * Garantiza longitud mínima y semántica clara.
 */
public final class ServiceDescription {

    public final String description;

    private ServiceDescription(String description) {
        if (description == null || description.trim().length() < 20) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_DESCRIPTION_INVALID, VOContext.DENTAL_SERVICES);
        }
        this.description = description.trim();
    }

    public static ServiceDescription of(String description) {
        return new ServiceDescription(description);
    }

    public String getValue() {
        return description;
    }


}
