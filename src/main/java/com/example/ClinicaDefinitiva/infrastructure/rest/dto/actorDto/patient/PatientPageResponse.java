package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient;

public record PatientPageResponse(
        Long patientId,
        Long contractId,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
