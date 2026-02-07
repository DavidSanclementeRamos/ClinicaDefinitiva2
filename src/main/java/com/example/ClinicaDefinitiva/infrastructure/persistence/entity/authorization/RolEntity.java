package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")   // tabla en español
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")   // columna en español
    private Long id;

    @Column(name = "codigo", nullable = false)
    private String rolEnum;    // atributo en inglés

    @Column(name = "descripcion", nullable = false)
    private String description; // atributo en inglés

    @Column(name = "es_predeterminado", nullable = false)
    private boolean isDefault;  // atributo en inglés

    @Column(name = "es_editable", nullable = false)
    private boolean isEditable; // atributo en inglés

    @Column(name = "es_eliminable", nullable = false)
    private boolean isDeletable; // atributo en inglés

    @Column(name = "estado", nullable = false)
    private String status;      // atributo en inglés (ej. ACTIVE, INACTIVE)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "rol_permisos",   // tabla intermedia en español
            joinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();

    public RolEntity() {}

    public RolEntity(Long id, String rolEnum, String description,
                     boolean isDefault, boolean isEditable,
                     boolean isDeletable, String status) {
        this.id = id;
        this.rolEnum = rolEnum;
        this.description = description;
        this.isDefault = isDefault;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.status = status;
    }

    // Getters y Setters en inglés
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRolEnum() { return rolEnum; }
    public void setRolEnum(String rolEnum) { this.rolEnum = rolEnum; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    public boolean isEditable() { return isEditable; }
    public void setEditable(boolean isEditable) { this.isEditable = isEditable; }

    public boolean isDeletable() { return isDeletable; }
    public void setDeletable(boolean isDeletable) { this.isDeletable = isDeletable; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Set<PermissionEntity> getPermissions() { return permissions; }
    public void setPermissions(Set<PermissionEntity> permissions) { this.permissions = permissions; }
}
