package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.rol;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.PermissionDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol.PermissionRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol.RolCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class RolWriteRestMapper {

    public  CreateRolDto toCreateDto(RolCreateRequest restDto) {
        return new CreateRolDto(
                restDto.rolEnum(),
                restDto.description(),
                restDto.isDefault(),
                restDto.isEditable(),
                restDto.isDeletable()
        );

    }

    // Mapper
    public PermissionDto toPermissionDto(PermissionRequest request) {
        return new PermissionDto(request.actionCode(),request.resourceCode()); // ejemplo
    }

}

