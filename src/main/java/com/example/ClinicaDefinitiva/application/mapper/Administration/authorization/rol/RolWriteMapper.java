package com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;

import java.util.HashSet;

public class RolWriteMapper {
    public Permission toPermission(PermissionDto permissionDto) {
        return null;
    }

    public Rol fromCreateDto(CreateRolDto dto) {
        return null;
    }

    /** public  Rol fromCreateDto(CreateRolDto dto) {
        return Rol.createCustom(
                RolEnum.valueOf(dto.rolEnum()),   // String → RolEnum
                dto.description(),
                new HashSet<>()                   // permisos vacíos al inicio
        );

    }

    public Rol fromCloneDto(CreateRolDto dto, Rol rol){
        return Rol.cloneRole(
                rol,
                dto.description()
        );
    }
    public Permission toPermission(PermissionDto dto) {
        return Permission.of(ResourceCatalog.of(
                ResourceCatalog.BasicResource.valueOf(dto.resourceCode())),
                ActionCatalog.of(ActionCatalog.BasicAction.valueOf(dto.actionCode()))); // crea VO con reglas de negocio
    }*/

}

