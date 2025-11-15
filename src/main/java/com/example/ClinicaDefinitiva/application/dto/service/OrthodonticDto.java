package com.example.ClinicaDefinitiva.application.dto.service;

import jakarta.validation.constraints.NotNull;

public class OrthodonticDto {
    @NotNull
    private String applianceType;
    private Integer treatmentDurationMonths;
    private Boolean requiresFollowup;

    public OrthodonticDto(String applianceType, Boolean requiresFollowup, Integer treatmentDurationMonths) {
        this.applianceType = applianceType;
        this.requiresFollowup = requiresFollowup;
        this.treatmentDurationMonths = treatmentDurationMonths;
    }

    public OrthodonticDto(){}
    public String getApplianceType() {
        return applianceType;
    }

    public void setApplianceType(String applianceType) {
        this.applianceType = applianceType;
    }

    public Boolean getRequiresFollowup() {
        return requiresFollowup;
    }

    public void setRequiresFollowup(Boolean requiresFollowup) {
        this.requiresFollowup = requiresFollowup;
    }

    public Integer getTreatmentDurationMonths() {
        return treatmentDurationMonths;
    }

    public void setTreatmentDurationMonths(Integer treatmentDurationMonths) {
        this.treatmentDurationMonths = treatmentDurationMonths;
    }
}
