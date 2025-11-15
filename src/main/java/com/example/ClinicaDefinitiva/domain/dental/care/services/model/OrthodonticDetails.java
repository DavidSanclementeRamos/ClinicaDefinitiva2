package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;

import java.util.Objects;

public final class OrthodonticDetails implements ServiceDetails {
    private final String applianceType;              // Tipo de aparato (brackets metálicos, cerámicos, alineadores)
    private final Integer treatmentDurationMonths;     // Duración estimada del tratamiento en meses
    private final boolean requiresFollowup;        //Indica si requiere controles periódicos

    public OrthodonticDetails(String applianceType, Integer treatmentDurationMonths, Boolean requiresFollowup) {
        if (applianceType == null || applianceType.isBlank()) throw new IllegalArgumentException("applianceType required");
        if (treatmentDurationMonths != null && treatmentDurationMonths <= 0) throw new IllegalArgumentException("treatmentDurationMonths must be > 0");
        this.applianceType = applianceType;
        this.treatmentDurationMonths = treatmentDurationMonths;
        this.requiresFollowup = Boolean.TRUE.equals(requiresFollowup);
    }

    @Override public ServiceType serviceType() { return ServiceType.ORTHODONTIC; }
    public String getApplianceType() { return applianceType; }
    public Integer getTreatmentDurationMonths() { return treatmentDurationMonths; }
    public Boolean getRequiresFollowup() { return requiresFollowup; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrthodonticDetails)) return false;
        OrthodonticDetails that = (OrthodonticDetails) o;
        return applianceType.equals(that.applianceType) && java.util.Objects.equals(treatmentDurationMonths, that.treatmentDurationMonths) && java.util.Objects.equals(requiresFollowup, that.requiresFollowup);
    }

    @Override public int hashCode() { return Objects.hash(applianceType, treatmentDurationMonths, requiresFollowup); }
}



