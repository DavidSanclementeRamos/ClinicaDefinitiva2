package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.administration.authorization.userRolAssignment;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.CreateAssignmentPermanentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.CreateAssignmentTemporaryRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRolAssignmentWriteMapper {

    // Permanent
    public  CreateAssignmentPermanentDto toServicePermanent(CreateAssignmentPermanentRequest restDto) {
        return new CreateAssignmentPermanentDto(
                restDto.userId(),
                restDto.rolId(),
                restDto.isPrimary()
        );
    }

    // Temporary
    public  CreateAssignmentTemporaryDto toServiceTemporary(CreateAssignmentTemporaryRequest restDto) {
        return new CreateAssignmentTemporaryDto(
                restDto.userId(),
                restDto.rolId(),
                restDto.isPrimary(),
                restDto.validFrom() ,
                restDto.validTo()
        );
    }
}

