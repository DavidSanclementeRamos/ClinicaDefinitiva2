package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.administration.authorization.rol;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.PageAssignmentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol.RolPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol.RolReadResponse;
import org.springframework.stereotype.Component;

@Component
public class RolReadMapper {

    public  RolReadResponse toResponse(ReadRolDto serviceDto) {
        return new RolReadResponse(
                serviceDto.id(),
                serviceDto.rolEnum(),
                serviceDto.description(),
                serviceDto.isDefault(),
                serviceDto.isEditable(),
                serviceDto.isDeletable(),
                serviceDto.statusRol(),
                serviceDto.permissions()
        );
    }

    public  RolPageResponse toPageResponse(PageRolDto serviceDto) {
        return new RolPageResponse(
                serviceDto.id(),
                serviceDto.rolEnum(),
                serviceDto.description(),
                serviceDto.isDefault(),
                serviceDto.isEditable(),
                serviceDto.isDeletable(),
                serviceDto.statusRol()
        );
    }
}

