package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;

import java.util.Objects;

public class PediatricDetails implements ServiceDetails {
    private final String ageRange;
    private final String behaviorManagement;
    private final String pediatricMaterials;

    public PediatricDetails(String ageRange, String behaviorManagement, String pediatricMaterials) {
        this.ageRange = ageRange;
        this.behaviorManagement = behaviorManagement;
        this.pediatricMaterials = pediatricMaterials;
    }

    @Override public ServiceType serviceType() { return ServiceType.PEDIATRICS; }
    public String getAgeRange() { return ageRange; }
    public String getBehaviorManagement() { return behaviorManagement; }
    public String getPediatricMaterials() { return pediatricMaterials; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof PediatricDetails)) return false; PediatricDetails that = (PediatricDetails)o; return Objects.equals(ageRange, that.ageRange) && Objects.equals(behaviorManagement, that.behaviorManagement) && Objects.equals(pediatricMaterials, that.pediatricMaterials); }
    @Override public int hashCode() { return Objects.hash(ageRange, behaviorManagement, pediatricMaterials); }


}
