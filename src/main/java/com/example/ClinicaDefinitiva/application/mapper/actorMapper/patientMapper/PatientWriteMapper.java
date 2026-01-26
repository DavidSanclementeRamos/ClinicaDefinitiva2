package com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Patient dtoCreateToPatient(CreatePatientDto dto) {
        UserIdentity user = new UserIdentity();

        return Patient.registerPatient(

                new Person(
                        new Address(dto.street(), dto.city(), dto.state(),
                                dto.country(), dto.postalCode()),
                        new Age(new DateOfBirth(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        new DateOfBirth(dto.dateOfBirth()),
                        new Document(dto.dni()),
                        dto.documentEPS(),
                        new FullName(dto.first(), dto.lastName()),
                        new PhoneNumber(dto.phoneNumber())
                ),

                user,
                GuardianId.fromLong(dto.guardianId())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateSensitiveToPatient(UpdatePatientSensitiveDto dto, Patient patient) {
        UserIdentity user = new UserIdentity();

        patient.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName()),
                user

        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateContactToPatient(UpdatePatientContactDto dto, Patient patient) {
        UserIdentity user = new UserIdentity();

        patient.updatePatientContact(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                new PhoneNumber(dto.phoneNumber()),
                user
        );
    }
}
