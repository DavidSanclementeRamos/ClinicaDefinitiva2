package com.example.ClinicaDefinitiva.application.dto.actor.dentist;


/**
 * DTO para actualizar información de contacto
 */
public record UpdateDentistContactDto (
        //Long dentistId,
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
){}
