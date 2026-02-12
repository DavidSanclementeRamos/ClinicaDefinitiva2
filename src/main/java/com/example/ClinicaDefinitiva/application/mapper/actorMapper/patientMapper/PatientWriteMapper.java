package com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Patient fromCreateDto(CreatePatientDto dto) {

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

                UserIdentityId.from( dto.userId()),
                GuardianId.fromLong(dto.guardianId()),
                ContractId.fromLong(dto.contractId())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void updateSensitiveFromDto(UpdatePatientSensitiveDto dto, Patient patient) {

        patient.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName())


        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void updateContactFromDto(UpdatePatientContactDto dto, Patient patient) {

        patient.updatePatientContact(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                new PhoneNumber(dto.phoneNumber())

        );
    }
}
