package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.rol;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleReadEntityMapper {

    public Rol toDomain(RoleEntity entity) {
        // Crear ID
        RolId rolId = RolId.of(entity.getId());
        
        // Mapear RolEnum desde String
        RolEnum rolEnum = RolEnum.valueOf(entity.getRoleType());
        
        // Mapear Status
        RolStatus status = RolStatus.valueOf(entity.getStatus());
        
        // Mapear Permisos - de String a Permission
        Set<Permission> permissions = new HashSet<>();
        for (String permissionStr : entity.getPermissions()) {
            // Asumiendo formato "ACTION_RESOURCE" ej: "CREATE_DENTIST"
            String[] parts = permissionStr.split("_", 2);
            if (parts.length == 2) {
                ActionCatalog action = ActionCatalog.custom(parts[0]);
                ResourceCatalog resource = ResourceCatalog.custom(parts[1]);
                permissions.add(Permission.of(resource, action));
            }
        }
        
        return Rol.reconstruct(
            rolId,
            rolEnum,
            entity.getDescription(),
            entity.isDefault(),
            entity.isEditable(),
            entity.isDeletable(),
            status,
            permissions,
            entity.getStateChangeReason()
        );
    }
}
