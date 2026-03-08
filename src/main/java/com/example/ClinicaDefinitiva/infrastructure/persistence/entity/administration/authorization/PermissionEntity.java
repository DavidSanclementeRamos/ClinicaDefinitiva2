package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PermissionEntity {
    @Column(name = "recurso", nullable = false)
    private String resource;

    @Column(name = "accion", nullable = false)
    private String action;

    public PermissionEntity() {
    }
    public PermissionEntity(String resource, String action) {
        this.resource = resource;
        this.action = action;
    }

    public PermissionEntity(ResourceCatalog resource, ActionCatalog action) {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
