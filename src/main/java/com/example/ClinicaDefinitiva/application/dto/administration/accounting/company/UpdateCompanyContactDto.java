package com.example.ClinicaDefinitiva.application.dto.administration.accounting.company;


/**
 * DTO para actualizar información de contacto
 */
public record UpdateCompanyContactDto(
        String name,
        String legalRepresentative,

        //AddressDto
        String street,
        String city,
        String state,
        String country,
        String postalCode,

        String phoneNumber,
        String email
) {}
