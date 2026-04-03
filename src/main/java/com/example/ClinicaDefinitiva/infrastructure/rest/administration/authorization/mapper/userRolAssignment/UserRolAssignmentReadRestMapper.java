package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.userRolAssignment;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.PageAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.PageAssignmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.ReadAssignmentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Component
public class UserRolAssignmentReadRestMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public  ReadAssignmentResponse toRest(ReadAssignmentDto dto) {
        return new ReadAssignmentResponse(
                dto.id(),
                dto.userId(),
                dto.rolId(),
                dto.isPrimary(),
                dto.validFrom() != null ? LocalDate.parse(dto.validFrom().format(FORMATTER)) : null,
                dto.validTo() != null ? LocalDate.parse(dto.validTo().format(FORMATTER)) : null
        );
    }

    public  PageAssignmentResponse toRestPage(PageAssignmentDto dto) {
        return new PageAssignmentResponse(
                dto.id(),
                dto.userId(),
                dto.rolId(),
                dto.isPrimary(),
                dto.validFrom() != null ? LocalDate.parse(dto.validFrom().format(FORMATTER)) : null,
                dto.validTo() != null ? LocalDate.parse(dto.validTo().format(FORMATTER)) : null
        );
    }
}
