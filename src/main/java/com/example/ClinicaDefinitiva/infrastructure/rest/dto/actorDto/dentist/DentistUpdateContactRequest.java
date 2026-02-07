package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;

/**
 * DTO para actualizar información de contacto
 */
public record DentistUpdateContactRequest(
        Long dentistId,
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber

        ) {}
