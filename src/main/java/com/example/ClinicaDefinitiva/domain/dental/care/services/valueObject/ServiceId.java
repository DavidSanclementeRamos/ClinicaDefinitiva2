package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public final class ServiceId {

    private final String value;

    private ServiceId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ServiceId generate() {
        return new ServiceId(UUID.randomUUID().toString());
    }

    public static ServiceId fromString(String value){
        if (value == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_ID_NULL, VOContext.SERVICE_ID);
        }
        String trimmed = value.trim();
        if(trimmed.isEmpty()) throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_ID_BLANK, VOContext.SERVICE_ID);
        return new ServiceId(trimmed);
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceId)) return false;
        ServiceId serviceId = (ServiceId) o;
        return value.equals(serviceId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    // Identificadores estáticos de servicios conocidos
    public static final ServiceId GENERAL_CONSULTATION = new ServiceId("SRV-0001");
    public static final ServiceId PROPHYLAXIS_CLEANING = new ServiceId("SRV-0002");

    public static final ServiceId ORTHO_METAL_BRACKETS = new ServiceId("ORT-0101");
    public static final ServiceId ORTHO_CLEAR_ALIGNERS = new ServiceId("ORT-0102");

    public static final ServiceId SURG_WISDOM_EXTRACTION = new ServiceId("SUR-0201");
    public static final ServiceId SURG_SOFT_TISSUE_GRAFT = new ServiceId("SUR-0202");

    public static final ServiceId PED_SEALANTS_FLUORIDE = new ServiceId("PED-0301");
    public static final ServiceId PED_RESIN_RESTORATION = new ServiceId("PED-0302");

    public static final ServiceId AES_IN_OFFICE_WHITENING = new ServiceId("AES-0401");
    public static final ServiceId AES_PORCELAIN_VENEER = new ServiceId("AES-0402");

    public static final ServiceId IMP_SINGLE_IMPLANT = new ServiceId("IMP-0501");
    public static final ServiceId PRO_PORCELAIN_CROWN = new ServiceId("PRO-0601");
}