package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authorization.rol;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol.PermissionRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol.RolCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class RolWriteMapper {

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

