package com.example.ClinicaDefinitiva.application.dto;

// DTOs auxiliares
public record AddressDto(
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {}