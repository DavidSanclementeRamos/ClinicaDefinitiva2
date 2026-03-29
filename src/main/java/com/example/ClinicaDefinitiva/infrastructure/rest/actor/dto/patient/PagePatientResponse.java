package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient;

public record PagePatientResponse(
        Long patientId,
        Long contractId,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
