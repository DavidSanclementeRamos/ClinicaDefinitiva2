package com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.PageAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentReadMapper {

    public  ReadAssignmentDto toReadDto(UserRolAssignment assignment) {
        return new ReadAssignmentDto(
                assignment.getId().getValue(),        // UserRolAssignmentId → Long
                assignment.getUserId().value(),    // UserId → Long
                assignment.getRolId().getValue(),     // RolId → Long
                assignment.isPrimary(),
                assignment.getValidFrom(),
                assignment.getValidTo()
        );
    }

    public  PageAssignmentDto toPageDto(UserRolAssignment assignment) {
        return new PageAssignmentDto(
                assignment.getId().getValue(),
                assignment.getUserId().value(),
                assignment.getRolId().getValue(),
                assignment.isPrimary(),
                assignment.getValidFrom(),
                assignment.getValidTo()
        );
    }
}

