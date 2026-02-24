package com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteMapper {

    public Patient fromCreateDto(CreatePatientDto dto) {
        return Patient.registerPatient(
                Person.of(
                        Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                        Age.of(DateOfBirth.of(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        DateOfBirth.of(dto.dateOfBirth()),
                        Document.of(dto.dni()),
                        dto.documentEPS(),
                        FullName.of(dto.first(), dto.lastName()),
                        PhoneNumber.of(dto.phoneNumber())
                ),
                UserIdentityId.from(dto.userId()),
                GuardianId.fromLong(dto.guardianId())
        );
    }

    public void updateSensitiveFromDto(UpdatePatientSensitiveDto dto, Patient patient) {
        patient.updateSensitiveData(
                Age.of(DateOfBirth.of(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                DateOfBirth.of(dto.dateOfBirth()),
                Document.of(dto.dni()),
                dto.documentEPS(),
                FullName.of(dto.first(), dto.lastName())
        );
    }

    public void updateContactFromDto(UpdatePatientContactDto dto, Patient patient) {
        patient.updatePatientContact(
                Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber())
        );
    }
}
