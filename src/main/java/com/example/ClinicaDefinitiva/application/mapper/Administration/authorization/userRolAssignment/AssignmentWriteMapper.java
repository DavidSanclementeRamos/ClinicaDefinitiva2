package com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.stereotype.Component;

@Component
public class AssignmentWriteMapper {

    public  UserRolAssignment fromCreatePermanent(CreateAssignmentPermanentDto dto) {
        return UserRolAssignment.assignPermanent(
                UserIdentityId.from(dto.userId()),             // Long → UserId VO
                RolId.of(dto.rolId()),               // Long → RolId VO
                dto.isPrimary()
        );
    }

    public  UserRolAssignment fromCreateTemporary(CreateAssignmentTemporaryDto dto) {
        return UserRolAssignment.assignTemporary(
                UserIdentityId.from(dto.userId()),
                RolId.of(dto.rolId()),
                dto.validFrom(),
                dto.validTo(),
                dto.isPrimary()
        );
    }
}

