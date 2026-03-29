package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "responsable")
public class GuardianEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_identidad", nullable = false)
    private UserIdentityEntity userIdentity;

    @Embedded
    private PersonEmbeddable person;

    @Column(name = "tipo_responsable", nullable = false, length = 30)
    private String guardianType;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    public GuardianEntity() {}

    public Long getId()                    { return id; }
    public UserIdentityEntity getUserIdentity() { return userIdentity; }
    public PersonEmbeddable getPerson()      { return person; }
    public String getGuardianType()          { return guardianType; }
    public LocalDateTime getLastUpdate()     { return lastUpdate; }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setUserIdentity(UserIdentityEntity userIdentity)  { this.userIdentity = userIdentity; }
    public void setPerson(PersonEmbeddable person)                { this.person = person; }
    public void setGuardianType(String guardianType)              { this.guardianType = guardianType; }
    public void setLastUpdate(LocalDateTime lastUpdate)           { this.lastUpdate = lastUpdate; }
}