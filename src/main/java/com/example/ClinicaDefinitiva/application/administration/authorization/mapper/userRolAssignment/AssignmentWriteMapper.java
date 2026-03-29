package com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class AssignmentWriteMapper {

    public UserIdentityId toUserIdentityId(CreateAssignmentPermanentDto dto) {
        return UserIdentityId.from(dto.userId());
    }

    public RolId toRolId(CreateAssignmentPermanentDto dto) {
        return RolId.of(dto.rolId());
    }

    public boolean toIsPrimary(CreateAssignmentPermanentDto dto) {
        return dto.isPrimary();
    }

    public UserIdentityId toUserIdentityId(CreateAssignmentTemporaryDto dto) {
        return UserIdentityId.from(dto.userId());
    }

    public RolId toRolId(CreateAssignmentTemporaryDto dto) {
        return RolId.of(dto.rolId());
    }

    public LocalDate toValidFrom(CreateAssignmentTemporaryDto dto) {
        return dto.validFrom();
    }

    public LocalDate toValidTo(CreateAssignmentTemporaryDto dto) {
        return dto.validTo();
    }

    public boolean toIsPrimary(CreateAssignmentTemporaryDto dto) {
        return dto.isPrimary();
    }
}

