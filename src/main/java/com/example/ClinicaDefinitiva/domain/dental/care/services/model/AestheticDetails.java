package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;

import java.util.Objects;

public class AestheticDetails implements ServiceDetails {
    private final String aestheticType;
    private final String materialUsed;
    private final String expectedResult;

    public AestheticDetails(String aestheticType, String materialUsed, String expectedResult) {
        if (aestheticType == null || aestheticType.isBlank()) throw new IllegalArgumentException("aestheticType required");
        this.aestheticType = aestheticType;
        this.materialUsed = materialUsed;
        this.expectedResult = expectedResult;
    }

    @Override public ServiceType serviceType() { return ServiceType.AESTHETICS; }
    public String getAestheticType() { return aestheticType; }
    public String getMaterialUsed() { return materialUsed; }
    public String getExpectedResult() { return expectedResult; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof AestheticDetails)) return false; AestheticDetails that = (AestheticDetails)o; return Objects.equals(aestheticType, that.aestheticType) && Objects.equals(materialUsed, that.materialUsed) && Objects.equals(expectedResult, that.expectedResult); }
    @Override public int hashCode() { return Objects.hash(aestheticType, materialUsed, expectedResult); }



}
