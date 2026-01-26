package com.example.ClinicaDefinitiva.application.dto.actor.Patient;



public record UpdatePatientContactDto (
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
){}
