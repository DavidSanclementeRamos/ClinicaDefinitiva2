package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_rol_usuario")
public class UserRoleAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_identidad", nullable = false)
    private UserIdentityEntity userIdentity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private RoleEntity role;

    @Column(name = "valido_desde", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valido_hasta")
    private LocalDate validUntil;

    @Column(name = "es_principal", nullable = false)
    private boolean isPrimary;

    public UserRoleAssignmentEntity() {}

    public Long getId()                      { return id; }
    public UserIdentityEntity getUserIdentity()   { return userIdentity; }
    public RoleEntity getRole()                { return role; }
    public LocalDate getValidFrom()            { return validFrom; }
    public LocalDate getValidUntil()           { return validUntil; }
    public boolean isPrimary()                 { return isPrimary; }

    public void setId(Long id) {
        this.id = id;
    }

    
    
    public void setUserIdentity(UserIdentityEntity userIdentity)  { this.userIdentity = userIdentity; }
    public void setRole(RoleEntity role)                          { this.role = role; }
    public void setValidFrom(LocalDate validFrom)                 { this.validFrom = validFrom; }
    public void setValidUntil(LocalDate validUntil)               { this.validUntil = validUntil; }
    public void setPrimary(boolean isPrimary)                     { this.isPrimary = isPrimary; }
}