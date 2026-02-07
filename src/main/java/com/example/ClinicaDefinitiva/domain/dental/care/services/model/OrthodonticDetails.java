package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.OrthodonticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Set;

public final class OrthodonticDetails implements ServiceDetails {
    private static final Set VALID_APPLIANCE_TYPES = Set.of(
            "METAL_BRACKETS",
            "CERAMIC_BRACKETS",
            "LINGUAL_BRACKETS",
            "CLEAR_ALIGNERS",
            "REMOVABLE_APPLIANCES",
            "FUNCTIONAL_APPLIANCES"
    );

    private final String applianceType;
    private final Integer treatmentDurationMonths;
    private final boolean requiresFollowup;

    public OrthodonticDetails(String applianceType, Integer treatmentDurationMonths, Boolean requiresFollowup) {
        // RN-ORTHODONTIC-001
        if (applianceType == null || applianceType.isBlank()) {
            throw new ValueObjectValidationException(
                    OrthodonticError.ERR_ORTHODONTIC_MISSING_APPLIANCE, VOContext.ORTHODONTIC

            );
        }

        // RN-ORTHODONTIC-003
        if (!VALID_APPLIANCE_TYPES.contains(applianceType.toUpperCase())) {
            throw new ValueObjectValidationException(
                    OrthodonticError.ERR_ORTHODONTIC_INVALID_APPLIANCE,VOContext.ORTHODONTIC
            );
        }

        // RN-ORTHODONTIC-004 y RN-ORTHODONTIC-002
        if (treatmentDurationMonths != null) {
            if (treatmentDurationMonths <= 0) {
                throw new ValueObjectValidationException(
                        OrthodonticError.ERR_ORTHODONTIC_NEGATIVE_DURATION,VOContext.ORTHODONTIC
                );
            }
            if (treatmentDurationMonths < 6 || treatmentDurationMonths > 48) {
                throw new ValueObjectValidationException(
                        OrthodonticError.ERR_ORTHODONTIC_INVALID_DURATION,VOContext.ORTHODONTIC
                );
            }
        }

        this.applianceType = applianceType.toUpperCase();
        this.treatmentDurationMonths = treatmentDurationMonths;
        this.requiresFollowup = Boolean.TRUE.equals(requiresFollowup);
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.ORTHODONTIC;
    }

    public String getApplianceType() {
        return applianceType;
    }

    public Integer getTreatmentDurationMonths() {
        return treatmentDurationMonths;
    }

    public Boolean getRequiresFollowup() {
        return requiresFollowup;
    }
}





