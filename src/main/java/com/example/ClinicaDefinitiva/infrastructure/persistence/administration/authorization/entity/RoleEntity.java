package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rol")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "tipo_rol", nullable = false, length = 50)
    private String roleType;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String description;

    @Column(name = "es_predeterminado", nullable = false)
    private boolean isDefault;

    @Column(name = "es_editable", nullable = false)
    private boolean isEditable;

    @Column(name = "es_eliminable", nullable = false)
    private boolean isDeletable;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    /**
     * Set<Permission> del dominio se persiste como colección de strings.
     * ADR-54: @ElementCollection evita crear una entidad de permiso innecesaria
     * ya que Permission es un Value Object sin identidad propia.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "rol_permiso",
        joinColumns = @JoinColumn(name = "id_rol")
    )
    @Column(name = "permiso", nullable = false, length = 100)
    private Set<String> permissions = new HashSet<>();
    
    @Column(name = "razon_estado", length = 500)
    private String stateChangeReason;

    public RoleEntity() {}

    public Long getId()                  { return id; }
    public String getRoleType()           { return roleType; }
    public String getDescription()        { return description; }
    public boolean isDefault()            { return isDefault; }
    public boolean isEditable()           { return isEditable; }
    public boolean isDeletable()          { return isDeletable; }
    public String getStatus()             { return status; }
    public Set<String> getPermissions()   { return permissions; }

    public String getStateChangeReason() {
        return stateChangeReason;
    }

    public void setStateChangeReason(String stateChangeReason) {
        this.stateChangeReason = stateChangeReason;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    
    public void setRoleType(String roleType)              { this.roleType = roleType; }
    public void setDescription(String description)        { this.description = description; }
    public void setDefault(boolean isDefault)             { this.isDefault = isDefault; }
    public void setEditable(boolean isEditable)           { this.isEditable = isEditable; }
    public void setDeletable(boolean isDeletable)         { this.isDeletable = isDeletable; }
    public void setStatus(String status)                  { this.status = status; }
    public void setPermissions(Set<String> permissions)   { this.permissions = permissions; }
}