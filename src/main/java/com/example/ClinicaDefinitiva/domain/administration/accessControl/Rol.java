package com.example.ClinicaDefinitiva.domain.administration.accessControl;

import com.example.ClinicaDefinitiva.domain.administration.accessControl.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.StatusRol;

public class Rol {

    private  Long id;
    private  RolEnum rolEnum;
    private  String description;
    private  boolean  isDefault;
    private  boolean isEditable;
    private  boolean isDeletable;
    private  StatusRol statusRol;

    public Rol() {
    }

    public Rol (RolEnum rolEnum, String description, boolean isDefault, boolean isEditable, boolean isDeletable, StatusRol statusRol) {
        this.rolEnum = rolEnum;
        this.description = description;
        this.isDefault = isDefault;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.statusRol = statusRol;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void setRolEnum(RolEnum rolEnum) {
        this.rolEnum = rolEnum;
    }

    public void setStatusRol(StatusRol statusRol) {
        this.statusRol = statusRol;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public RolEnum getRolEnum() {
        return rolEnum;
    }

    public StatusRol getStatusRol() {
        return statusRol;
    }
}
