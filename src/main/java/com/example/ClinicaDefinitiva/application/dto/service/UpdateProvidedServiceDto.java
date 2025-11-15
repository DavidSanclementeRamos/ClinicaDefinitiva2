package com.example.ClinicaDefinitiva.application.dto.service;

import java.math.BigDecimal;

public class UpdateProvidedServiceDto {
    public String name;
    public ServiceCatalogDto catalog;
    public BigDecimal baseRateAmount;
    public String baseRateCurrency;
    public Integer durationMinutes;
    public Boolean requiresAuthorization;
    public String description;
    public String status;

    // details (opcionales)
    public CreateProvidedServiceDto.OrthodonticDto orthodontic;
    public CreateProvidedServiceDto.ProstheticDto prosthetic;
    public CreateProvidedServiceDto.ImplantologyDto implantology;
    public CreateProvidedServiceDto.AestheticDto aesthetic;
    public CreateProvidedServiceDto.PediatricDto pediatric;
    public CreateProvidedServiceDto.SurgicalDto surgical;



}
