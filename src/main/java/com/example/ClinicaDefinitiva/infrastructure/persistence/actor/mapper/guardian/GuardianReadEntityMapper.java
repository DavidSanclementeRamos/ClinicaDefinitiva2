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

    // --- CORRECCIÓN DE DIRECCIÓN ---
    String addressStr = embeddable.getAddress();
   
    String[] parts = addressStr.split("\\|");
       Address address = Address.of(
        parts[0].trim(),
        parts[1].trim(),
        parts[2].trim(),
        parts[3].trim(),
        parts[4].trim()
    );

    PhoneNumber phoneNumber = PhoneNumber.of(embeddable.getPhoneNumber());
    DateOfBirth dateOfBirth = DateOfBirth.of(embeddable.getBirthDate());
    Age age = Age.of(dateOfBirth);
    BloodType bloodType = BloodType.fromLabel(embeddable.getBloodType());
    Document document = Document.of(embeddable.getDocumentNumber());

    // --- CORRECCIÓN DEL NOMBRE COMPLETO (evita error si no hay espacio) ---
    String fullNameStr = embeddable.getFullName();
    String firstName, lastName;
    int spaceIdx = fullNameStr.indexOf(' ');
    if (spaceIdx > 0) {
        firstName = fullNameStr.substring(0, spaceIdx);
        lastName = fullNameStr.substring(spaceIdx + 1);
    } else {
        firstName = fullNameStr;
        lastName = "";
    }
    FullName fullName = FullName.of(firstName, lastName);

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