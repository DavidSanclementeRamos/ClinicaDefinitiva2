package com.example.ClinicaDefinitiva.application.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServiceCatalogDto {
    @NotNull @NotBlank public String id;
    @NotNull @NotBlank public String name;
    @NotNull
    @NotBlank
    public String category;


}
