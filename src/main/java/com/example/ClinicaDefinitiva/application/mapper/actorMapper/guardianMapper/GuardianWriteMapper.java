package com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class GuardianWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Guardian dtoCreateToGuardian(CreateGuardianDto dto) {
        UserIdentity user = new UserIdentity();

        return Guardian.registerGuardian(
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
                new TypeGuardian(dto.code(),dto.description())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateSensitiveToGuardian(UpdateGuardianSensitiveDto dto, Guardian guardian) {
        UserIdentity user = new UserIdentity();

        guardian.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName()),
                user,
             new TypeGuardian(dto.code(),dto.description())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateContactToGuardian(UpdateGuardianContactDto dto, Guardian guardian) {
        UserIdentity user = new UserIdentity();

        guardian.updateContactData(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                new PhoneNumber(dto.phoneNumber()),
                user
        );
    }

}

