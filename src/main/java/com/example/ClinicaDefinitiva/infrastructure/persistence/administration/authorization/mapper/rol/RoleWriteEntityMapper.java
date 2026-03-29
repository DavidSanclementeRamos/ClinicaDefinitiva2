package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.rol;


import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleWriteEntityMapper {

    public RoleEntity toEntity(Rol domain) {
        RoleEntity entity = new RoleEntity();
        
        // ID
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        
        // Datos básicos
        entity.setRoleType(domain.getRolEnum().name());
        entity.setDescription(domain.getDescription());
        
        // Flags
        entity.setDefault(domain.isDefault());
        entity.setEditable(domain.isEditable());
        entity.setDeletable(domain.isDeletable());
        
        // Status
        entity.setStatus(domain.getStatusRol().name());
        
        // Permisos - convertir Permission a String (ACTION_RESOURCE)
        Set<String> permissionStrings = new HashSet<>();
        for (Permission permission : domain.getPermissions()) {
            permissionStrings.add(permission.getCode());
        }
        entity.setPermissions(permissionStrings);
        
          entity.setStateChangeReason(domain.getLastStateChangeReason());

        
        return entity;
    }
}