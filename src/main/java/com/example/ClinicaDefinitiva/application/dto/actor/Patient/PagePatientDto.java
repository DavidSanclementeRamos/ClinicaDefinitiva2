package com.example.ClinicaDefinitiva.application.dto.actor.Patient;

public record PagePatientDto (
        Long patientId,
        Long contractId,
        String dni,
        String first,
        String lastName,
        String phoneNumber
){}
