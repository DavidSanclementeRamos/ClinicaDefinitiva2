package com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;

import java.util.stream.Collectors;

public class RolReadMapper {

    public  ReadRolDto toReadDto(Rol rol) {
        return new ReadRolDto(
                rol.getId().getValue(),          // RolId → Long
                rol.getRolEnum().name(),         // RolEnum → String
                rol.getDescription(),
                rol.isDefault(),
                rol.isEditable(),
                rol.isDeletable(),
                rol.getStatusRol().name(),       // RolStatus → String
                rol.getPermissions().stream()
                        .map(Permission::getCode)    // Permission → String code
                        .collect(Collectors.toSet())
        );
    }

    public  PageRolDto toPageDto(Rol rol) {
        return new PageRolDto(
                rol.getId().getValue(),
                rol.getRolEnum().name(),
                rol.getDescription(),
                rol.isDefault(),
                rol.isEditable(),
                rol.isDeletable(),
                rol.getStatusRol().name()
        );
    }
}

