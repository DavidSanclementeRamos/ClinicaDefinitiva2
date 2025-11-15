package com.example.ClinicaDefinitiva.application.dto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReadProvidedServiceDto {
    public String id;
    public String name;
    public String catalogId;
    public String catalogName;
    public String catalogCategory;
    public String code;
    public BigDecimal baseRateAmount;
    public String baseRateCurrency;
    public Integer durationMinutes;
    public Boolean requiresAuthorization;
    public String description;
    public String status;
    public String serviceType;
    public CreateProvidedServiceDto.OrthodonticDto orthodontic;
    public CreateProvidedServiceDto.ProstheticDto prosthetic;
    public CreateProvidedServiceDto.ImplantologyDto implantology;
    public CreateProvidedServiceDto.AestheticDto aesthetic;
    public CreateProvidedServiceDto.PediatricDto pediatric;
    public CreateProvidedServiceDto.SurgicalDto surgical;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}