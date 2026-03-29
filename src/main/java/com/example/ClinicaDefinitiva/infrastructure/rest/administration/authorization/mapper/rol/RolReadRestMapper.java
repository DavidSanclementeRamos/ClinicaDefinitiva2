package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.rol;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol.RolPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol.RolReadResponse;
import org.springframework.stereotype.Component;

@Component
public class RolReadRestMapper {

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

