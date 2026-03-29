package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.Patient;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientReadEntityMapper {

    public Patient toDomain(PatientEntity entity) {
        Person person = mapToPerson(entity.getPerson());
        
        PatientId patientId = PatientId.of(entity.getId());
        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUserIdentity().getId());
        GuardianId guardianId = entity.getGuardian() != null ? 
            GuardianId.fromLong(entity.getGuardian().getId()) : null;
        ContractId contractId = entity.getContract() != null ? 
            ContractId.of(entity.getContract().getId()) : null;
        
        // Lista vacía de tratamientos (se cargaría aparte)
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
    
    private Person mapToPerson(PersonEmbeddable embeddable) {
        if (embeddable == null) return null;
        
        Address address = Address.of(embeddable.getAddress(), "", "", "", "");
        PhoneNumber phoneNumber = PhoneNumber.of(embeddable.getPhoneNumber());
        DateOfBirth dateOfBirth = DateOfBirth.of(embeddable.getBirthDate());
        Age age = Age.of(dateOfBirth);
        BloodType bloodType = BloodType.fromLabel(embeddable.getBloodType());
        Document document = Document.of(embeddable.getDocumentNumber());
        FullName fullName = FullName.of(
            embeddable.getFullName().split(" ")[0],
            embeddable.getFullName().substring(embeddable.getFullName().indexOf(" ") + 1)
        );
        
        return Person.of(
            address,
            age,
            bloodType,
            dateOfBirth,
            document,
            embeddable.getEpsDocument(),
            fullName,
            phoneNumber
        );
    }
}