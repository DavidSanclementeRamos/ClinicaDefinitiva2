package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Agregado Rol - representa una instancia de rol con permisos asignados
 * Puede ser persistido y modificado en runtime
 */
public class Rol {

    private  RolId id;
    private final RolEnum rolEnum;
    private final String description;
    private final boolean isDefault;
    private final boolean isEditable;
    private final boolean isDeletable;
    private  RolStatus statusRol;

    // Permisos asignados dinámicamente
    private Set<Permission> permissions;



    private Rol(RolEnum rolEnum, String description, boolean isDefault,
               boolean isEditable, boolean isDeletable, RolStatus statusRol) {
        this.rolEnum = rolEnum ;
        this.description = description;
        this.isDefault = isDefault;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.statusRol = statusRol;
        this.permissions = new HashSet<>();
    }
    
        /** Crea un rol institucional/base del sistema (no editable ni eliminable). */
    public static Rol createDefault(RolEnum baseType, String description) {
        return new Rol(baseType, description, true, false, false, RolStatus.ACTIVE);
    }

    

    /** Clona un rol existente con nueva descripción (editable y eliminable). */
    public static Rol cloneFrom(Rol sourceRole, String newDescription) {
        return new Rol(sourceRole.getRolEnum(), newDescription, false, true, true, RolStatus.ACTIVE);
    }


    public void addPermission(Permission permission) {
        ensureEditable();

        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        ensureEditable();
        if(this.permissions.isEmpty()){
            throw new BusinessRuleViolationException( RolError.RR_ROL_EMPTY_PERMISSIONS, EntityContext.ROL );
            
        }

        this.permissions.remove(permission);
    }


    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }


    public void setPermissions(Set<Permission> permissions) {
        ensureEditable();
        this.permissions = new HashSet<>(permissions);
    }

    public void delete() {
        if (!isDeletable){
            throw new BusinessRuleViolationException(RolError.ERR_ROL_SYSTEM_NOT_DELETABLE, EntityContext.ROL);
        }
         if (this.statusRol != RolStatus.DELETED) {
        throw new BusinessRuleViolationException(
                RolError.ERR_ROL_DELETE_NOT_MARKED,
                EntityContext.ROL
        );
         }
    }
    private void ensureEditable() {
        if (!isEditable) {
            throw new BusinessRuleViolationException( RolError.ERR_ROL_SYSTEM_NOT_EDITABLE, EntityContext.ROL );
        }
    }

    public void activate(String reason) {
        validateStateChange(reason);
        this.statusRol = RolStatus.ACTIVE;
    }

    public void deactivate(String reason) {
        validateStateChange(reason);
        this.statusRol = RolStatus.INACTIVE;
    }

    public void suspend(String reason) {
        validateStateChange(reason);
        this.statusRol = RolStatus.SUSPENDED;
    }

    public void markDeleted(String reason) {
        if (!isDeletable) {
            throw new BusinessRuleViolationException(
                    RolError.ERR_ROL_SYSTEM_NOT_DELETABLE,
                    EntityContext.ROL
            );
        }
        validateStateChange(reason);
        this.statusRol = RolStatus.DELETED;
    }


    private void validateStateChange(String reason) {
        if (reason == null || reason.trim().length() < 10) {
            throw new BusinessRuleViolationException(
                    RolError.ERR_ROL_DELETE_REASON_REQUIRED,
                    EntityContext.ROL
            );
        }

    }

    public RolId getId() { return id; }
    public RolEnum getRolEnum() { return rolEnum; }
    public String getDescription() { return description; }
    public boolean isDefault() { return isDefault; }
    public boolean isEditable() { return isEditable; }
    public boolean isDeletable() { return isDeletable; }
    public RolStatus getStatusRol() { return statusRol; }
    public Set<Permission> getPermissions() { return new HashSet<>(permissions); }


    public void setId(RolId targetRoleId) {
    }
}
