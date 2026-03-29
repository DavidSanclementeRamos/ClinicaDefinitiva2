
package com.example.ClinicaDefinitiva.application.actor.dto.patient;


public record PagePatientDto (
        Long patientId,
        Long contractId,
        Long userId,
        String dni,
        String first,
        String lastName,
        String phoneNumber
){}
