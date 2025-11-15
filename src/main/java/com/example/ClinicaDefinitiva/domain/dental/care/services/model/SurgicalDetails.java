package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;

import java.util.Objects;

public final class SurgicalDetails implements ServiceDetails {
    private final String surgeryType;
    private final String complexityLevel;
    private final Boolean requiresAnesthesia;
    private final Boolean operatingRoomNeeded;

    public SurgicalDetails(String surgeryType, String complexityLevel, Boolean requiresAnesthesia, Boolean operatingRoomNeeded) {
        this.surgeryType = surgeryType;
        this.complexityLevel = complexityLevel;
        this.requiresAnesthesia = Boolean.TRUE.equals(requiresAnesthesia);
        this.operatingRoomNeeded = Boolean.TRUE.equals(operatingRoomNeeded);
    }

    @Override public ServiceType serviceType() { return ServiceType.SURGERY; }
    public String getSurgeryType() { return surgeryType; }
    public String getComplexityLevel() { return complexityLevel; }
    public Boolean getRequiresAnesthesia() { return requiresAnesthesia; }
    public Boolean getOperatingRoomNeeded() { return operatingRoomNeeded; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof SurgicalDetails)) return false; SurgicalDetails that = (SurgicalDetails)o; return Objects.equals(surgeryType, that.surgeryType) && Objects.equals(complexityLevel, that.complexityLevel) && Objects.equals(requiresAnesthesia, that.requiresAnesthesia) && Objects.equals(operatingRoomNeeded, that.operatingRoomNeeded); }
    @Override public int hashCode() { return Objects.hash(surgeryType, complexityLevel, requiresAnesthesia, operatingRoomNeeded); }


}
