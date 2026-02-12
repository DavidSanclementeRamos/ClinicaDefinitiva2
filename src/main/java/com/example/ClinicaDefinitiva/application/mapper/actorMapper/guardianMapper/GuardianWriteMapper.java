package com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.stereotype.Component;

@Component
public class GuardianWriteMapper {

    public Guardian fromCreateDto(CreateGuardianDto dto) {
        return Guardian.registerGuardian(
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
                TypeGuardian.of(dto.code(), dto.description())
        );
    }

    public void updateSensitiveFromDto(UpdateGuardianSensitiveDto dto, Guardian guardian) {
        guardian.updateSensitiveData(
                Age.of(DateOfBirth.of(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                DateOfBirth.of(dto.dateOfBirth()),
                Document.of(dto.dni()),
                dto.documentEPS(),
                FullName.of(dto.first(), dto.lastName()),
                TypeGuardian.of(dto.code(), dto.description())
        );
    }

    public void updateContactFromDto(UpdateGuardianContactDto dto, Guardian guardian) {
        guardian.updateContactData(
                Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber())
        );
    }
}
