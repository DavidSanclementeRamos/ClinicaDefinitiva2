package com.example.ClinicaDefinitiva.application.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateProvidedServiceDto {
    @NotNull @NotBlank public String name;
    @NotNull public String serviceType; // ORTHODONTIC, SURGICAL, etc.
    @NotNull public ServiceCatalogDto catalog;
    @NotNull @NotBlank public String code;
    public BigDecimal baseRateAmount;
    public String baseRateCurrency;
    public Integer durationMinutes;
    public Boolean requiresAuthorization;
    @NotNull @NotBlank
    public String description;
    public String status;

    // Optional specific details (solo uno según serviceType)
   // public ServiceType serviceType;
    public OrthodonticDto orthodontic;
    public ProstheticDto prosthetic;
    public ImplantologyDto implantology;
    public AestheticDto aesthetic;
    public PediatricDto pediatric;
    public SurgicalDto surgical;

    // nested DTOs
    public static class OrthodonticDto {
        @NotNull @NotBlank public String applianceType;
        public Integer treatmentDurationMonths;
        public Boolean requiresFollowup;
    }
    public static class ProstheticDto {
        public String fixedOrRemovable;
        public String material;
        public String prostheticType;
        public Integer units;
    }
    public static class ImplantologyDto {
        public Integer healingTimeMonths;
        public String implantType;
        public String placementSite;
        public Boolean requiresBoneGraft;
    }
    public static class AestheticDto {
        public String aestheticType;
        public String materialUsed;
        public String expectedResult;
    }
    public static class PediatricDto {
        public String ageRange;
        public String behaviorManagement;
        public String pediatricMaterials;
    }
    public static class SurgicalDto {
        public String surgeryType;
        public String complexityLevel;
        public Boolean requiresAnesthesia;
        public Boolean operatingRoomNeeded;
    }
}
