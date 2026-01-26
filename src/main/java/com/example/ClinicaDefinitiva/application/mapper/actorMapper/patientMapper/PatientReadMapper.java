package com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientReadMapper {

    // dominio → DTO de lectura
    public ReadPatientDto toDto(Patient patient) {
        return new ReadPatientDto(
                patient.getPatientId().getValue(),
                patient.getGuardianId().getValue(),
                patient.getContractId().asLong(),
                patient.getPerson().getDni().toString(),
                patient.getPerson().getFullname().FirstName(),
                patient.getPerson().getFullname().LastName(),
                patient.getPerson().getAge().toString(),
                patient.getPerson().getPhoneNumber().toString(),
                patient.getPerson().getDateOfBirth().asDate(),
                patient.getPerson().getBloodType().getValue(),
                patient.getPerson().getDocumentoEPS(),
                patient.getUser().getValue(),
                patient.getLastUpdate(),
                patient.getPerson().getAddress().Street(),
                patient.getPerson().getAddress().City(),
                patient.getPerson().getAddress().State(),
                patient.getPerson().getAddress().Country(),
                patient.getPerson().getAddress().PostalCode()
        );
    }

    // dominio → DTO de lectura resumido (ej. para paginación)
    public PagePatientDto pageToDto(Patient patient) {
        return new PagePatientDto(
                patient.getPatientId().getValue(),
                patient.getContractId().asLong(),
                patient.getPerson().getDni().toString(),
                patient.getPerson().getFullname().FirstName(),
                patient.getPerson().getFullname().LastName(),
                patient.getPerson().getPhoneNumber().toString()
        );
    }
}
