package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recepcionista")
public class ReceptionistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_identidad", nullable = false)
    private UserIdentityEntity userIdentity;

    @Embedded
    private PersonEmbeddable person;

    @Column(name = "sector", length = 50)
    private String sector;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    public ReceptionistEntity() {}

    public Long getId()                    { return id; }
    public UserIdentityEntity getUserIdentity() { return userIdentity; }
    public PersonEmbeddable getPerson()      { return person; }
    public String getSector()                { return sector; }
    public LocalDateTime getLastUpdate()     { return lastUpdate; }

    public void setId(Long id) {
        this.id = id;
    }

    
    
    public void setUserIdentity(UserIdentityEntity userIdentity)  { this.userIdentity = userIdentity; }
    public void setPerson(PersonEmbeddable person)                { this.person = person; }
    public void setSector(String sector)                          { this.sector = sector; }
    public void setLastUpdate(LocalDateTime lastUpdate)           { this.lastUpdate = lastUpdate; }
}