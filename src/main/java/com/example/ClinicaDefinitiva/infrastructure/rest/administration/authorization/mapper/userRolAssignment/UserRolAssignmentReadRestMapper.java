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

    public  ReadAssignmentResponse toRest(ReadAssignmentDto serviceDto) {
        return new ReadAssignmentResponse(
                serviceDto.id(),
                serviceDto.userId(),
                serviceDto.rolId(),
                serviceDto.isPrimary(),
                serviceDto.validFrom() != null ? LocalDate.parse(serviceDto.validFrom().format(FORMATTER)) : null,
                serviceDto.validTo() != null ? LocalDate.parse(serviceDto.validTo().format(FORMATTER)) : null
        );
    }

    public  PageAssignmentResponse toRestPage(PageAssignmentDto serviceDto) {
        return new PageAssignmentResponse(
                serviceDto.id(),
                serviceDto.userId(),
                serviceDto.rolId(),
                serviceDto.isPrimary(),
                serviceDto.validFrom() != null ? LocalDate.parse(serviceDto.validFrom().format(FORMATTER)) : null,
                serviceDto.validTo() != null ? LocalDate.parse(serviceDto.validTo().format(FORMATTER)) : null
        );
    }
}
