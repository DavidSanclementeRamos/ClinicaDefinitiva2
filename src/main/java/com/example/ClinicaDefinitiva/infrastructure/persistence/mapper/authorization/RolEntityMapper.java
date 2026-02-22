package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.authorization;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.RoleBasedPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization.RolEntity;

import java.util.HashSet;
import java.util.Set;


public class RolEntityMapper {
    RolRepository Y;
    // Entity → Dominio
    public  Rol toDomain(RolEntity entity) {
      return new Rol(
                RolEnum.valueOf(entity.getRolEnum()),   // String → Enum
                entity.getDescription(),
true,
true,
true, null
      );



        // Permisos NO se persisten, se asignan en runtime


    }

    // Dominio → Entity
    public  RolEntity toEntity(Rol domain) {
        return new RolEntity(
                domain.getId() != null ? domain.getId().getValue() : null, // VO → Long
                domain.getRolEnum().name(),                               // Enum → String
                domain.getDescription(),
                domain.isDefault(),
                domain.isEditable(),
                domain.isDeletable(),
                domain.getStatusRol().name()                              // Enum → String
        );


    }
}


