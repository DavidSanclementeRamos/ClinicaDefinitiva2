package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;

import java.util.Objects;

public class ImplantologyDetails implements ServiceDetails {
    private final Integer healingTimeMonths;
    private final String implantType;
    private final String placementSite;
    private final Boolean requiresBoneGraft;

    public ImplantologyDetails(Integer healingTimeMonths, String implantType, String placementSite, Boolean requiresBoneGraft) {
        if (healingTimeMonths != null && healingTimeMonths < 0) throw new IllegalArgumentException("healingTimeMonths must be >= 0");
        this.healingTimeMonths = healingTimeMonths;
        this.implantType = implantType;
        this.placementSite = placementSite;
        this.requiresBoneGraft = Boolean.TRUE.equals(requiresBoneGraft);
    }

    @Override public ServiceType serviceType() { return ServiceType.IMPLANTOLOGY; }
    public Integer getHealingTimeMonths() { return healingTimeMonths; }
    public String getImplantType() { return implantType; }
    public String getPlacementSite() { return placementSite; }
    public Boolean getRequiresBoneGraft() { return requiresBoneGraft; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof ImplantologyDetails)) return false; ImplantologyDetails that = (ImplantologyDetails)o; return Objects.equals(healingTimeMonths, that.healingTimeMonths) && Objects.equals(implantType, that.implantType) && Objects.equals(placementSite, that.placementSite) && Objects.equals(requiresBoneGraft, that.requiresBoneGraft); }
    @Override public int hashCode() { return Objects.hash(healingTimeMonths, implantType, placementSite, requiresBoneGraft); }


}
