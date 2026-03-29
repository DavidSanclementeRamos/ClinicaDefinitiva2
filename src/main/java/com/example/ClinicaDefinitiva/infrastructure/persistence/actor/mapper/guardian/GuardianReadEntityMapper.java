package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.guardian;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.GuardianEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GuardianReadEntityMapper {

    public Guardian toDomain(GuardianEntity entity) {
        Person person = mapToPerson(entity.getPerson());
        
        GuardianId guardianId = GuardianId.fromLong(entity.getId());
        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUserIdentity().getId());
        TypeGuardian typeGuardian = TypeGuardian.fromCode(entity.getGuardianType());
        
        // Lista vacía de pacientes (se cargaría aparte si es necesario)
        List<PatientId> patientList = new ArrayList<>();
        
        return Guardian.reconstruct(
            guardianId,
            person,
            typeGuardian,
            userIdentityId,
            patientList,
            entity.getLastUpdate()
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