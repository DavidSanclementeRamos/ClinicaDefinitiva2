
package com.example.ClinicaDefinitiva.application.actor.dto.patient;

public record UpdatePatientContactDto (
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
){}
