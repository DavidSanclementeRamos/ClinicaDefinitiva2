package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.userRolAssignment;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRolAssignmentWriteRestMapper {

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

