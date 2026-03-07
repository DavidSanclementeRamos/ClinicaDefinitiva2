package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.administration.authorization.userRolAssignment;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.PageAssignmentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.PageAssignmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.ReadAssignmentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Component
public class UserRolAssignmentReadMapper {

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
