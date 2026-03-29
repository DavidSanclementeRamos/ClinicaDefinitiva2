package com.example.ClinicaDefinitiva.application.administration.authorization.mapper.rol;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.PermissionDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import org.springframework.stereotype.Component;

@Component
public class RolWriteMapper {
    

    public Permission toPermission(PermissionDto dto) {
        return Permission.of(ResourceCatalog.of(
                ResourceCatalog.BasicResource.valueOf(dto.resourceCode())),
                ActionCatalog.of(ActionCatalog.BasicAction.valueOf(dto.actionCode()))); // crea VO con reglas de negocio
    }

}

