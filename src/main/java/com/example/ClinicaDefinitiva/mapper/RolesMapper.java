package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.RolesDto;
import com.example.ClinicaDefinitiva.persistence.entity.RolesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolesMapper {
    @Mapping(target = "status", source = "status")
    RolesDto ReadRolesDto (RolesEntity rolesEntity);
}
