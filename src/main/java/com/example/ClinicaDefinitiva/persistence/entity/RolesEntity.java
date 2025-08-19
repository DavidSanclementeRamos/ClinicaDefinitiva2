package com.example.ClinicaDefinitiva.persistence.entity;

import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Permisos;
import com.example.ClinicaDefinitiva.Enum.Roles;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class RolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private final Roles roleEnum;

    @ElementCollection(targetClass = Permisos.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_permisos",
            joinColumns = @JoinColumn(name = "rol_id"))
    @Enumerated(EnumType.STRING)
    private final List<Permisos> permissionList ;

    private final String description;
    private final boolean  isDefault;
    private final boolean isEditable;
    private final boolean isDeletable;
    private final Estado status;


    public RolesEntity(   String description, boolean isDefault, boolean isEditable, boolean isDeletable,List<Permisos> permissionList,Roles roleEnum,Estado status) {
        //this.id = id;
        this.roleEnum = roleEnum;
        this.permissionList = permissionList;
        this.description = description;
        this.isDefault = isDefault;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.status = status;
    }


    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public List<Permisos> getPermissionList() {
        return permissionList;
    }

    public Roles getRoleEnum() {
        return roleEnum;
    }

    public Estado getStatus() {
        return status;
    }
}

