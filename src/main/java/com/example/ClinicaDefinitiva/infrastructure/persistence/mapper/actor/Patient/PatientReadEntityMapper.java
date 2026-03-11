package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.Patient;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PatientReadEntityMapper {

    public Patient toDomain(PatientEntity entity) {
        Address address = Address.of(
            entity.getStreet(),
            entity.getCity(),
            entity.getState(),
            entity.getCountry(),
            entity.getPostalCode()
        );

        DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.parse(entity.getDateOfBirth()));
        
        Age age = Age.of(dateOfBirth);

        PhoneNumber phoneNumber = PhoneNumber.of(entity.getPhoneNumber());

        BloodType bloodType = BloodType.fromLabel(entity.getBloodType());

        Document document = Document.of(entity.getDni());

        FullName fullName = FullName.of(entity.getFirst(), entity.getLastName());

        Person person = Person.of(
            address,
            age,
            bloodType,
            dateOfBirth,
            document,
            entity.getDocumentEPS(),
            fullName,
            phoneNumber
        );

        PatientId patientId = PatientId.of(entity.getPatientId());

        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUser());

        GuardianId guardianId = entity.getGuardian() != null 
            ? GuardianId.fromLong(entity.getGuardian().getGuardianId()) 
            : null;

        ContractId contractId = entity.getContractId() != null 
            ? ContractId.of(entity.getContractId()) 
            : null;

        List<TreatmentId> treatments = new ArrayList<>();

        return Patient.reconstruct(
            patientId,
            userIdentityId,
            guardianId,
            entity.getLastUpdate(),
            contractId,
            treatments,
            person
        );
    }
}