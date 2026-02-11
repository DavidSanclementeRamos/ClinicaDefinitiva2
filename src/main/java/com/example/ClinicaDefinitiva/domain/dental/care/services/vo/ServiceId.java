package com.example.ClinicaDefinitiva.domain.dental.care.services.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public final class ServiceId {

    //private final String value;
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
// Identificadores estáticos de servicios conocidos
   /* public static final ServiceId GENERAL_CONSULTATION = new ServiceId("SRV-0001", id);
    public static final ServiceId PROPHYLAXIS_CLEANING = new ServiceId("SRV-0002", id);

    public static final ServiceId ORTHO_METAL_BRACKETS = new ServiceId("ORT-0101", id);
    public static final ServiceId ORTHO_CLEAR_ALIGNERS = new ServiceId("ORT-0102", id);

    public static final ServiceId SURG_WISDOM_EXTRACTION = new ServiceId("SUR-0201", id);
    public static final ServiceId SURG_SOFT_TISSUE_GRAFT = new ServiceId("SUR-0202", id);

    public static final ServiceId PED_SEALANTS_FLUORIDE = new ServiceId("PED-0301", id);
    public static final ServiceId PED_RESIN_RESTORATION = new ServiceId("PED-0302", id);

    public static final ServiceId AES_IN_OFFICE_WHITENING = new ServiceId("AES-0401", id);
    public static final ServiceId AES_PORCELAIN_VENEER = new ServiceId("AES-0402", id);

    public static final ServiceId IMP_SINGLE_IMPLANT = new ServiceId("IMP-0501", id);
    public static final ServiceId PRO_PORCELAIN_CROWN = new ServiceId("PRO-0601", id);


    */
}