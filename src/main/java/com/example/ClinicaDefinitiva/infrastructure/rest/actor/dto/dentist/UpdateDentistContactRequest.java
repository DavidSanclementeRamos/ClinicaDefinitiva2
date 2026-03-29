package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;

/**
 * DTO para actualizar información de contacto
 */
public record UpdateDentistContactRequest(
        Long dentistId,
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber

        ) {}
