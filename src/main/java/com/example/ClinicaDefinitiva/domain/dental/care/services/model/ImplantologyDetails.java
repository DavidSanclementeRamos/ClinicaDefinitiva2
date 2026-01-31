package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.Enum.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ImplantologyError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ImplantologyDetails implements ServiceDetails {
    private static final int MIN_HEALING_MONTHS = 2;
    private static final int MAX_HEALING_MONTHS = 12;
    private static final int MIN_HEALING_WITH_GRAFT = 4;

    private final Integer healingTimeMonths;
    private final String implantType;
    private final String placementSite;
    private final boolean requiresBoneGraft;

    public ImplantologyDetails(Integer healingTimeMonths, String implantType,
                               String placementSite, Boolean requiresBoneGraft) {
        // RN-IMPLANTOLOGY-003 y RN-IMPLANTOLOGY-001
        if (healingTimeMonths != null) {
            if (healingTimeMonths < 0) {
                throw new ValueObjectValidationException(
                        ImplantologyError.ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME, VOContext.IMPLANTOLOGY
                );
            }

            if (healingTimeMonths < MIN_HEALING_MONTHS || healingTimeMonths > MAX_HEALING_MONTHS) {
                throw new ValueObjectValidationException(
                        ImplantologyError.ERR_IMPLANTOLOGY_INVALID_HEALING_TIME,VOContext.IMPLANTOLOGY
                );
            }
        }

        boolean needsGraft = Boolean.TRUE.equals(requiresBoneGraft);

        // RN-IMPLANTOLOGY-002
        if (needsGraft && healingTimeMonths != null && healingTimeMonths < MIN_HEALING_WITH_GRAFT) {
            throw new ValueObjectValidationException(
                    ImplantologyError.ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH,VOContext.IMPLANTOLOGY
            );
        }

        // RN-IMPLANTOLOGY-007
        if (placementSite != null && placementSite.length() < 2) {
            throw new ValueObjectValidationException(
                    ImplantologyError.ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE,VOContext.IMPLANTOLOGY
            );
        }

        this.healingTimeMonths = healingTimeMonths;
        this.implantType = implantType;
        this.placementSite = placementSite;
        this.requiresBoneGraft = needsGraft;
    }

    public ServiceType serviceType() {
        return ServiceType.IMPLANTOLOGY;
    }

    public Integer getHealingTimeMonths() {
        return healingTimeMonths;
    }

    public String getImplantType() {
        return implantType;
    }

    public String getPlacementSite() {
        return placementSite;
    }

    public Boolean getRequiresBoneGraft() {
        return requiresBoneGraft;
    }

}
