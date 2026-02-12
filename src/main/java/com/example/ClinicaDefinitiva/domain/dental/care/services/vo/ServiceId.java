package com.example.ClinicaDefinitiva.domain.dental.care.services.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public final class ServiceId {


    private final Long id;


    private ServiceId( Long id) {
        if (id == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_ID_NULL, VOContext.SERVICE_ID);
        }
        this.id = id;
    }


    public static ServiceId of(Long id) {
        if (id == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_ID_NULL, VOContext.SERVICE_ID);
        }
        return new ServiceId( id);
    }

    public Long getId() {
        return id;
    }

}